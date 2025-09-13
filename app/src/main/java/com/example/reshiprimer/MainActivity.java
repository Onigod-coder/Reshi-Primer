package com.example.reshiprimer;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    
    private TextView exampleTextView;
    private EditText answerEditText;
    private Button startButton;
    private Button checkButton;
    private TextView statsTextView;
    private LinearLayout exampleLayout;
    
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
        
        // Восстанавливаем состояние при повороте
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
    }
    
    private void generateExample() {
        operand1 = random.nextInt(90) + 10; // 10-99
        operand2 = random.nextInt(90) + 10; // 10-99
        
        String[] operators = {"+", "-", "*", "/"};
        operator = operators[random.nextInt(4)];
        
        // Для деления обеспечиваем целое частное
        if (operator.equals("/")) {
            operand1 = operand2 * (random.nextInt(8) + 1); // operand1 кратно operand2
        }
        
        correctAnswer = calculateAnswer(operand1, operand2, operator);
        
        String example = operand1 + " " + operator + " " + operand2 + " = ?";
        exampleTextView.setText(example);
        
        // Сброс состояния
        answerEditText.setText("");
        answerEditText.setEnabled(true);
        checkButton.setEnabled(true);
        isAnswerChecked = false;
        isExampleGenerated = true;
        
        // Обновляем состояние кнопок
        startButton.setEnabled(false);
        exampleLayout.setBackgroundColor(getResources().getColor(android.R.color.white));
        
        updateStats();
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
            
        } catch (NumberFormatException e) {
            // Неверный формат числа
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
        
        // Сохраняем состояние
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
        
        // Восстанавливаем состояние кнопок и полей
        startButton.setEnabled(!isExampleGenerated || isAnswerChecked);
        answerEditText.setEnabled(isExampleGenerated && !isAnswerChecked);
        checkButton.setEnabled(isExampleGenerated && !isAnswerChecked);
        
        // Восстанавливаем цвет фона
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
}
