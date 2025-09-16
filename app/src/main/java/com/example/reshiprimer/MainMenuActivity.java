package com.example.reshiprimer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {

    private Button newGameButton;
    private Button continueButton;
    private Button exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        newGameButton = findViewById(R.id.newGameButton);
        continueButton = findViewById(R.id.continueButton);
        exitButton = findViewById(R.id.exitButton);

        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearSavedState();
                Intent i = new Intent(MainMenuActivity.this, MainActivity.class);
                i.putExtra("continue_game", false);
                startActivity(i);
            }
        });

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainMenuActivity.this, MainActivity.class);
                i.putExtra("continue_game", true);
                startActivity(i);
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });
    }

    private void clearSavedState() {
        SharedPreferences sp = getSharedPreferences("math_prefs", MODE_PRIVATE);
        sp.edit().clear().apply();
    }
}
