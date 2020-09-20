package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameScreen extends AppCompatActivity implements View.OnClickListener {
    private Button[][] buttons = new Button[3][3];
    private ArrayList<String> rounds = new ArrayList<String>();
    private boolean player1Turn = true;
    private int roundCount;
    private int player1Points;
    private int player2Points;
    private TextView textViewPlayer1;
    private TextView textViewPlayer2;
    Button button_backmenu;
    String playerName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screen);
        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_view_p2);
        playerName = getIntent().getStringExtra("playerName");
        textViewPlayer1.setText(playerName + " : ");
        textViewPlayer2.setText("Bilgisayar : ");
        button_backmenu = (Button) findViewById(R.id.button_backmenu);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }
        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBoard();
            }
        });
        button_backmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {
            return;
        }
        ((Button) v).setText("X");

        roundCount++;
        if (checkForWin()) {
            if (player1Turn) {
                player1Wins();
            } else {
                player2Wins();
            }
        } else if (roundCount == 9) {
            draw();
        } else {
            player1Turn = !player1Turn;
        }

        if (!isFieldsFull()) {
            setForComputer();
        }

        roundCount++;
        if (checkForWin()) {
            if (player1Turn) {
                player1Wins();
            } else {
                player2Wins();
            }
        } else if (roundCount == 9) {
            draw();
        } else {
            player1Turn = !player1Turn;
        }
    }

    public boolean isFieldsFull() {
        int fieldsNumber = 0;
        String[][] field = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (!TextUtils.isEmpty(buttons[i][j].getText())) {
                    fieldsNumber++;
                }
            }
        }

        if (fieldsNumber == 9) {
            return true;
        }
        return false;
    }

    private boolean checkForWin() {
        String[][] field = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field[i][j] = buttons[i][j].getText().toString();
            }
        }
        for (int i = 0; i < 3; i++) {
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("")) {
                buttons[i][0].setTextColor(Color.parseColor("#28df99"));
                buttons[i][1].setTextColor(Color.parseColor("#28df99"));
                buttons[i][2].setTextColor(Color.parseColor("#28df99"));
                return true;
            }
        }
        for (int i = 0; i < 3; i++) {
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("")) {
                buttons[0][i].setTextColor(Color.parseColor("#28df99"));
                buttons[1][i].setTextColor(Color.parseColor("#28df99"));
                buttons[2][i].setTextColor(Color.parseColor("#28df99"));
                return true;
            }
        }
        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("")) {
            buttons[0][0].setTextColor(Color.parseColor("#28df99"));
            buttons[1][1].setTextColor(Color.parseColor("#28df99"));
            buttons[2][2].setTextColor(Color.parseColor("#28df99"));
            return true;
        }
        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("")) {
            return true;
        }
        return false;
    }

    public void setForComputer() {
        Random cell = new Random();
        Random column = new Random();

        int cellNumber = (cell.nextInt(3 - 0) + 0);
        int columnNumber = (column.nextInt(3 - 0) + 0);
        if (!TextUtils.isEmpty(buttons[cellNumber][columnNumber].getText().toString())) {
            setForComputer();
        } else {
            buttons[cellNumber][columnNumber].setText("O");
        }
    }

    private void player1Wins() {
        player1Points++;
        Toast.makeText(this, playerName +" kazandı!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        addGameDataToArray();
        //resetBoard();
    }

    private void player2Wins() {
        player2Points++;
        Toast.makeText(this, "Bilgisayar kazandı!", Toast.LENGTH_SHORT).show();
        updatePointsText();
        addGameDataToArray();
        //resetBoard();
    }

    private void draw() {
        Toast.makeText(this, "Berabere!", Toast.LENGTH_SHORT).show();
        addGameDataToArray();
        //resetBoard();
    }

    private void updatePointsText() {
        textViewPlayer1.setText(playerName + " : " + player1Points);
        textViewPlayer2.setText("Bilgisayar : " + player2Points);
    }

    private void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setTextColor(Color.parseColor("#000000"));
                buttons[i][j].setText("");
            }
        }
        roundCount = 0;
        player1Turn = true;
    }

    private void addGameDataToArray() {
        String sessionId = getIntent().getStringExtra("documentID");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> round = new HashMap<>();
        round.put("player1Point", player1Points);
        round.put("player2Point", player2Points);
        round.put("gameId",sessionId);
        db.collection("rounds").add(round);
    }
}