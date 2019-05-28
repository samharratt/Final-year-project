package com.example.home.optometryapplication;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizActivity extends AppCompatActivity {
    public static final String EXTRA_SCORE = "myScore";
    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_ANSWERED = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";
    //initialisation
    private TextView textViewQuestion;
    private TextView textViewScore;
    private TextView textViewQuestionCount;
    private RadioGroup rbGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private Button buttonConfirmNext;
    private ColorStateList textColorDefaultRb;
    private ArrayList<Question> questionList;
    private Question currentQuestion;
    private int questionCounter;
    private int questionCountTotal;
    private int score;
    private boolean answered;
    private long backPressedTime;
    //onCreate called when activity is open
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        //Initialising to layout view ids
        textViewQuestion = findViewById(R.id.text_view_question);
        textViewScore = findViewById(R.id.text_view_score);
        textViewQuestionCount = findViewById(R.id.text_view_question_count);
        rbGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        buttonConfirmNext = findViewById(R.id.button_confirm_next);
        //gets color for radio buttons
        textColorDefaultRb = rb1.getTextColors();

        if (savedInstanceState == null) { //only execute if there is no savedInstancedState
            QuizDbHelper dbHelper = new QuizDbHelper(this);
            questionList = dbHelper.getAllQuestions(); //gets questions
            questionCountTotal = questionList.size(); //counts total questions in db for counter
            Collections.shuffle(questionList); // Used to shuffle questions in db
            showNextQuestion();
        } else {
            questionList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST); //Will restore as it was saved
            if (questionList == null) {
                finishQuiz();
            } //if no question list finish activity

            questionCountTotal = questionList.size(); //calculate question list totals
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT); //
            currentQuestion = questionList.get(questionCounter - 1); //Question counter is always 1 ahead of question index
            score = savedInstanceState.getInt(KEY_SCORE);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED);

            if (!answered) {
                showSolution();
            }
        }

        buttonConfirmNext.setOnClickListener(new View.OnClickListener() {   //onclicklistener for radio buttons, ensures radio button is checked
            @Override
            public void onClick(View v) {
                if (!answered) {
                    if (rb1.isChecked() || rb2.isChecked() || rb3.isChecked()) {
                        checkAnswer();
                    } else {
                        Toast.makeText(QuizActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show(); //ensures user selects a radio box
                    }
                } else { //else continue to next question
                    showNextQuestion();
                }
            }
        });
    }
    //when next question is displayed, radio buttons is reset
    private void showNextQuestion() {
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rbGroup.clearCheck();   //clears previous radio button input
        if (questionCounter < questionCountTotal) { //if counter number is lower than total questions, keep counting
            currentQuestion = questionList.get(questionCounter);
            textViewQuestion.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());  //sets text for answer 1
            rb2.setText(currentQuestion.getOption2());  //sets text for answer 2
            rb3.setText(currentQuestion.getOption3());  //sets text for answer 3

            questionCounter++; //Counter increments to get all questions
            textViewQuestionCount.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answered = false; //ensures it records answer instead of skipping question
            buttonConfirmNext.setText("Confirm");
        } else {
            finishQuiz();   //Else counter number is larger than the total question number, finish
        }
    }
    //method for checking if question was answered
    private void checkAnswer() {
        answered = true; //set to true as question was answered

        RadioButton rbSelected = findViewById(rbGroup.getCheckedRadioButtonId()); //returns ID of which rb was checked
        int answerNr = rbGroup.indexOfChild(rbSelected) + 1;

        if (answerNr == currentQuestion.getAnswerNr()) { //compares selected answerNr to answerNr which is correct
            score++;    //if correct increase score by 1
            textViewScore.setText("Score: " + score);
        }
        showSolution();
    }
    //method for showing the correct color for answers
    private void showSolution() { //displays incorrect as red
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        //displays text and changes RB colors
        switch (currentQuestion.getAnswerNr()) { //displays correct answer as green
            case 1:
                rb1.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer A is correct");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer B is correct");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                textViewQuestion.setText("Answer C is correct");
                break;
        }
        // When question count is higher than total, change edit text to finish
        if (questionCounter < questionCountTotal) {
            buttonConfirmNext.setText("Next"); //keep text as next when there are still questions in the DB
        } else {
            buttonConfirmNext.setText("Finish"); //change set text to "finish" when out of questions
        }
    }
    //finish quiz  method opens new intent and passes highscore to textview
    private void finishQuiz() {
        Intent resultIntent = new Intent(); //sends results back to quizHome
        resultIntent.putExtra(EXTRA_SCORE, score);
        setResult(RESULT_OK, resultIntent);
        finish();
    }
    //method for leaving the quiz midway through. Used to stop going back by accident.
    @Override
    public void onBackPressed() { //when back is pressed twice within 3 seconds exit quiz  activity
        if (backPressedTime + 3000 > System.currentTimeMillis()) {
            finishQuiz();
        } else {
            Toast.makeText(this, "Press back again to finish the quiz", Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SCORE, score);//put values into score
        outState.putInt(KEY_QUESTION_COUNT, questionCounter);//
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionList);
    }
}