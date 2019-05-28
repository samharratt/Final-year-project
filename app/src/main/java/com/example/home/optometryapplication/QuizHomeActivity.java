package com.example.home.optometryapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class QuizHomeActivity extends AppCompatActivity {
    //Share Prefs
    private static final int REQUEST_CODE_QUIZ = 1;
    public static final String SHARED_PREFERENCES = "sharedPreferences";
    public static final String HIGHSCORE = "quizHighscore";
    //Variables
    private TextView textViewHighScore;
    private int highscore;
    //OnCreate Load the current high score and set OnClick buttons
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_home);
        textViewHighScore = findViewById(R.id.text_view_highscore);
        loadHighscores();
        Button buttonStartQuiz = findViewById(R.id.button_start_quiz);
        buttonStartQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuiz();
            }
        });

    }
    //Start quiz method opens new activity
    private void startQuiz() {
        Intent intent = new Intent(QuizHomeActivity.this, QuizActivity.class);
        startActivityForResult(intent, REQUEST_CODE_QUIZ);
    }
    //Updates highscore
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //used to save highscores and score(gets results from quiz activity)
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_QUIZ) { //compares if requestCode equals REQUEST_CODE_QUIZ
            if (resultCode == RESULT_OK) {
                int score = data.getIntExtra(QuizActivity.EXTRA_SCORE, 0);  //this is where the integer for score is saved
                if (score > highscore) { //if score is larger than current highscore update highscore
                    updateHighscores(score);
                }

            }
        }
    }

    private void loadHighscores() {   //method for loading highscores from previous attempts
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        highscore = sharedPreferences.getInt(HIGHSCORE, 0); //default highscore value is 0
        textViewHighScore.setText("Highscore: +" + highscore);
    }

    //method for updating highscores
    private void updateHighscores(int highscoreNew) { //method for updating highscore
        highscore = highscoreNew; //set variable to new as it can increase
        textViewHighScore.setText("Highscore: " + highscore);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit(); //editor is used to save variables in sharedPreferences
        editor.putInt(HIGHSCORE, highscore);
        editor.apply();
    }
}
