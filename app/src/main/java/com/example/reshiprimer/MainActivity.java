package com.example.reshiprimer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    
    private static final String PREFS_NAME = "math_prefs";
    private static final String KEY_OPERAND1 = "operand1";
    private static final String KEY_OPERAND2 = "operand2";
    private static final String KEY_OPERATOR = "operator";
    private static final String KEY_CORRECT_ANSWER = "correctAnswer";
    private static final String KEY_CORRECT_COUNT = "correctCount";
    private static final String KEY_TOTAL_COUNT = "totalCount";
    private static final String KEY_IS_EXAMPLE_GENERATED = "isExampleGenerated";
    private static final String KEY_IS_ANSWER_CHECKED = "isAnswerChecked";
    private static final String KEY_EXAMPLE_TEXT = "exampleText";
    private static final String KEY_ANSWER_TEXT = "answerText";
    private static final String KEY_STATS_TEXT = "statsText";

    private TextView exampleTextView;
    private EditText answerEditText;
    private Button startButton;
    private Button checkButton;
    private TextView statsTextView;
    private LinearLayout exampleLayout;
    private Button menuButton;
    
    private int operand1, operand2;
    private String operator;
    private int correctAnswer;
    private int correctCount = 0;
    private int totalCount = 0;
    private boolean isExampleGenerated = false;
    private boolean isAnswerChecked = false;
    
    private Random random = new Random();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initializeViews();
        setupClickListeners();
        
        // Handle launch mode: new game or continue
        Intent intent = getIntent();
        boolean continueGame = intent != null && intent.getBooleanExtra("continue_game", false);
        if (continueGame) {
            loadFromPrefs();
        }
        
        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        }
    }
    
    private void initializeViews() {
        exampleTextView = findViewById(R.id.exampleTextView);
        answerEditText = findViewById(R.id.answerEditText);
        startButton = findViewById(R.id.startButton);
        checkButton = findViewById(R.id.checkButton);
        statsTextView = findViewById(R.id.statsTextView);
        exampleLayout = findViewById(R.id.exampleLayout);
        menuButton = findViewById(R.id.menuButton);
    }
    
    private void setupClickListeners() {
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateExample();
            }
        });
        
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });
        
        if (menuButton != null) {
            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveToPrefs();
                    Intent i = new Intent(MainActivity.this, MainMenuActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
                    finish();
                }
            });
        }
    }
    
    private void generateExample() {
        operand1 = random.nextInt(90) + 10; // 10-99
        operand2 = random.nextInt(90) + 10; // 10-99
        
        String[] operators = {"+", "-", "*", "/"};
        operator = operators[random.nextInt(4)];
        
        if (operator.equals("/")) {
            operand1 = operand2 * (random.nextInt(8) + 1); // ensure divisible
        }
        
        correctAnswer = calculateAnswer(operand1, operand2, operator);
        
        String example = operand1 + " " + operator + " " + operand2 + " = ?";
        exampleTextView.setText(example);
        
        answerEditText.setText("");
        answerEditText.setEnabled(true);
        checkButton.setEnabled(true);
        isAnswerChecked = false;
        isExampleGenerated = true;
        
        startButton.setEnabled(false);
        exampleLayout.setBackgroundColor(getResources().getColor(android.R.color.white));
        
        updateStats();
        saveToPrefs();
    }
    
    private int calculateAnswer(int op1, int op2, String op) {
        switch (op) {
            case "+": return op1 + op2;
            case "-": return op1 - op2;
            case "*": return op1 * op2;
            case "/": return op1 / op2;
            default: return 0;
        }
    }
    
    private void checkAnswer() {
        if (!isExampleGenerated || isAnswerChecked) return;
        
        String userAnswerStr = answerEditText.getText().toString().trim();
        if (userAnswerStr.isEmpty()) return;
        
        try {
            int userAnswer = Integer.parseInt(userAnswerStr);
            totalCount++;
            
            if (userAnswer == correctAnswer) {
                correctCount++;
                exampleLayout.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
            } else {
                exampleLayout.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
            }
            
            isAnswerChecked = true;
            answerEditText.setEnabled(false);
            checkButton.setEnabled(false);
            startButton.setEnabled(true);
            
            updateStats();
            saveToPrefs();
            
        } catch (NumberFormatException e) {
            // ignore
        }
    }
    
    private void updateStats() {
        double percentage = totalCount > 0 ? (double) correctCount / totalCount * 100 : 0;
        String stats = "Правильных: " + correctCount + "\nНеправильных: " + (totalCount - correctCount) + 
                      "\nПроцент правильных: " + String.format("%.2f", percentage) + "%";
        statsTextView.setText(stats);
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("operand1", operand1);
        outState.putInt("operand2", operand2);
        outState.putString("operator", operator);
        outState.putInt("correctAnswer", correctAnswer);
        outState.putInt("correctCount", correctCount);
        outState.putInt("totalCount", totalCount);
        outState.putBoolean("isExampleGenerated", isExampleGenerated);
        outState.putBoolean("isAnswerChecked", isAnswerChecked);
        outState.putString("exampleText", exampleTextView.getText().toString());
        outState.putString("answerText", answerEditText.getText().toString());
        outState.putString("statsText", statsTextView.getText().toString());
    }
    
    private void restoreState(Bundle savedInstanceState) {
        operand1 = savedInstanceState.getInt("operand1");
        operand2 = savedInstanceState.getInt("operand2");
        operator = savedInstanceState.getString("operator");
        correctAnswer = savedInstanceState.getInt("correctAnswer");
        correctCount = savedInstanceState.getInt("correctCount");
        totalCount = savedInstanceState.getInt("totalCount");
        isExampleGenerated = savedInstanceState.getBoolean("isExampleGenerated");
        isAnswerChecked = savedInstanceState.getBoolean("isAnswerChecked");
        
        exampleTextView.setText(savedInstanceState.getString("exampleText"));
        answerEditText.setText(savedInstanceState.getString("answerText"));
        statsTextView.setText(savedInstanceState.getString("statsText"));
        
        startButton.setEnabled(!isExampleGenerated || isAnswerChecked);
        answerEditText.setEnabled(isExampleGenerated && !isAnswerChecked);
        checkButton.setEnabled(isExampleGenerated && !isAnswerChecked);
        
        if (isAnswerChecked) {
            String userAnswerStr = answerEditText.getText().toString().trim();
            if (!userAnswerStr.isEmpty()) {
                try {
                    int userAnswer = Integer.parseInt(userAnswerStr);
                    if (userAnswer == correctAnswer) {
                        exampleLayout.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
                    } else {
                        exampleLayout.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light));
                    }
                } catch (NumberFormatException e) {
                    exampleLayout.setBackgroundColor(getResources().getColor(android.R.color.white));
                }
            }
        } else {
            exampleLayout.setBackgroundColor(getResources().getColor(android.R.color.white));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveToPrefs();
    }

    private void saveToPrefs() {
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putInt(KEY_OPERAND1, operand1);
        ed.putInt(KEY_OPERAND2, operand2);
        ed.putString(KEY_OPERATOR, operator);
        ed.putInt(KEY_CORRECT_ANSWER, correctAnswer);
        ed.putInt(KEY_CORRECT_COUNT, correctCount);
        ed.putInt(KEY_TOTAL_COUNT, totalCount);
        ed.putBoolean(KEY_IS_EXAMPLE_GENERATED, isExampleGenerated);
        ed.putBoolean(KEY_IS_ANSWER_CHECKED, isAnswerChecked);
        ed.putString(KEY_EXAMPLE_TEXT, exampleTextView != null ? exampleTextView.getText().toString() : "");
        ed.putString(KEY_ANSWER_TEXT, answerEditText != null ? answerEditText.getText().toString() : "");
        ed.putString(KEY_STATS_TEXT, statsTextView != null ? statsTextView.getText().toString() : "");
        ed.apply();
    }

    private void loadFromPrefs() {
        SharedPreferences sp = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        operand1 = sp.getInt(KEY_OPERAND1, 0);
        operand2 = sp.getInt(KEY_OPERAND2, 0);
        operator = sp.getString(KEY_OPERATOR, null);
        correctAnswer = sp.getInt(KEY_CORRECT_ANSWER, 0);
        correctCount = sp.getInt(KEY_CORRECT_COUNT, 0);
        totalCount = sp.getInt(KEY_TOTAL_COUNT, 0);
        isExampleGenerated = sp.getBoolean(KEY_IS_EXAMPLE_GENERATED, false);
        isAnswerChecked = sp.getBoolean(KEY_IS_ANSWER_CHECKED, false);
        String ex = sp.getString(KEY_EXAMPLE_TEXT, "");
        String ans = sp.getString(KEY_ANSWER_TEXT, "");
        String st = sp.getString(KEY_STATS_TEXT, "");

        exampleTextView.setText(ex);
        answerEditText.setText(ans);
        statsTextView.setText(st);

        startButton.setEnabled(!isExampleGenerated || isAnswerChecked);
        answerEditText.setEnabled(isExampleGenerated && !isAnswerChecked);
        checkButton.setEnabled(isExampleGenerated && !isAnswerChecked);
        exampleLayout.setBackgroundColor(getResources().getColor(android.R.color.white));
    }
}


