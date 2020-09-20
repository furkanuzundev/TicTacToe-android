package com.example.tictactoe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button selectOnePlayer, pressPlayGame, selectTwoPlayer, matchHistoryButton;
    LinearLayout onePlayerLayout, twoPlayerLayout;
    public EditText onePlayer_name, twoPlayer_firstPlayerName, twoPlayer_secondPlayerName;
    int howManyPlayer;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectOnePlayer = (Button) findViewById(R.id.selectOnePlayer);
        pressPlayGame = (Button) findViewById(R.id.pressPlayGame);
        selectTwoPlayer = (Button) findViewById(R.id.selectTwoPlayer);
        onePlayerLayout = (LinearLayout) findViewById(R.id.onePlayerLayout);
        twoPlayerLayout = (LinearLayout) findViewById(R.id.twoPlayerLayout);
        onePlayer_name = (EditText) findViewById(R.id.onePlayer_name);
        twoPlayer_firstPlayerName = (EditText) findViewById(R.id.twoPlayer_firstPlayerName);
        twoPlayer_secondPlayerName = (EditText) findViewById(R.id.twoPlayer_secondPlayerName);


        selectOnePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                howManyPlayer = 1;
                twoPlayerLayout.setVisibility(View.GONE);
                onePlayerLayout.setVisibility(View.VISIBLE);
                pressPlayGame.setVisibility(View.VISIBLE);
            }
        });

        selectTwoPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                howManyPlayer = 2;
                onePlayerLayout.setVisibility(View.GONE);
                twoPlayerLayout.setVisibility(View.VISIBLE);
                pressPlayGame.setVisibility(View.VISIBLE);
            }
        });
        pressPlayGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (howManyPlayer == 1) {
                    passToOnePlayerScreen();
                } else {
                    passToTwoPlayerScreen();
                }
            }
        });
    }

    public void passToOnePlayerScreen() {
        String onePlayer_playerName = onePlayer_name.getText().toString();
        if (TextUtils.isEmpty(onePlayer_playerName)) {
            onePlayer_name.setError("Oyuncu ismi boş geçilemez.");
        } else {
            addOnePlayerToFirebase();
        }
    }

    public void passToTwoPlayerScreen() {
        String firstName = twoPlayer_firstPlayerName.getText().toString();
        String secondName = twoPlayer_secondPlayerName.getText().toString();
        if (TextUtils.isEmpty(firstName)) {
            twoPlayer_firstPlayerName.setError("1. oyuncu ismi boş geçilemez.");
        } else if (TextUtils.isEmpty(secondName)) {
            twoPlayer_secondPlayerName.setError("2. oyuncu ismi boş geçilemez.");
        } else {
            addTwoPlayerToFirebase();
        }
    }

    public void clearComponents() {
        onePlayerLayout.setVisibility(View.GONE);
        twoPlayerLayout.setVisibility(View.GONE);
        pressPlayGame.setVisibility(View.GONE);
        onePlayer_name.setText("");
        twoPlayer_firstPlayerName.setText("");
        twoPlayer_secondPlayerName.setText("");
    }

    public void addOnePlayerToFirebase() {
        Map<String, Object> game = new HashMap<>();
        game.put("status", "playComputer");
        game.put("playerName", (onePlayer_name.getText()).toString());
        game.put("gameName", (onePlayer_name.getText()).toString() + "VS" + "Computer");

        db.collection("games")
                .add(game)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Intent intent = new Intent(MainActivity.this, GameScreen.class);
                        intent.putExtra("documentID", documentReference.getId());
                        intent.putExtra("playerName", onePlayer_name.getText().toString());
                        startActivity(intent);
                        clearComponents();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.println(Log.DEBUG, "test", "failed : " + e);
                    }
                });
    }

    public void addTwoPlayerToFirebase() {
        Map<String, Object> game = new HashMap<>();
        game.put("status", "playFriend");
        game.put("player1Name", (twoPlayer_firstPlayerName.getText()).toString());
        game.put("player2Name", (twoPlayer_secondPlayerName.getText()).toString());
        game.put("gameName", (twoPlayer_firstPlayerName.getText()).toString() + "VS" + (twoPlayer_secondPlayerName.getText()).toString());

        db.collection("games")
                .add(game)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Intent intent = new Intent(MainActivity.this, GameScreenTwoPlayer.class);
                        intent.putExtra("documentID", documentReference.getId());
                        intent.putExtra("player1Name", twoPlayer_firstPlayerName.getText().toString());
                        intent.putExtra("player2Name", twoPlayer_secondPlayerName.getText().toString());

                        startActivity(intent);
                        clearComponents();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.println(Log.DEBUG, "test", "failed : " + e);
                    }
                });
    }
}



