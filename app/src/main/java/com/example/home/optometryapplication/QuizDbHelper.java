package com.example.home.optometryapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.home.optometryapplication.QuizContract.*;

import java.util.ArrayList;
import java.util.List;

public class QuizDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MyQuiz.db"; //Names database
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;

    public QuizDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //oncreate create the DB
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        //creates initial db
        final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                QuestionsTable.TABLE_NAME + " ( " +
                QuestionsTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QuestionsTable.COLUMN_QUESTION + " TEXT, " +
                QuestionsTable.COLUMN_OPTION1 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION2 + " TEXT, " +
                QuestionsTable.COLUMN_OPTION3 + " TEXT, " +
                QuestionsTable.COLUMN_ANSWER_NR + " INTEGER" +
                ")";

        db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
        fillQuestionsTable(); //Calls fillQuestion method to fill the table with data.
    }

    @Override //used to update database
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + QuestionsTable.TABLE_NAME);
        onCreate(db);
    }

    private void fillQuestionsTable() { //fills database with data
        Question q1 = new Question("What describes the distance from the back of the\n" +
                "corrective lens to the cornea?", "A.  Pantoscopic angle", "B.  Fitting triangle", "C.  Vertex distance", 3);
        addQuestion(q1);
        Question q2 = new Question("When writing a prescription, which abbreviation\n" +
                "stands for once every day?", "A.  Bid", "B.  Qid", "C.  Qd", 3);
        addQuestion(q2);
        Question q3 = new Question("What is the onset for the medication Opthetic\n" +
                "using 0.5% solution?", "A.  15-60 minutes", "B.  30-60 minutes", "C.  10-30 seconds", 3);
        addQuestion(q3);
        Question q4 = new Question("Which instrument provides laser-based, noncontact, non-invasive imaging of the retina?",
                "A.  Optical Coherence Tomography Scanner", "B.  Fundus Photography", "C.  Pachymetry", 1);
        addQuestion(q4);
        Question q5 = new Question("What does the root word phot mean?",
                "A.  Light", "B.  Color", "C.  Disease", 1);
        addQuestion(q5);
//        Question q5 = new Question("B is correct again", "A", "B", "C", 2);
//        addQuestion(q5);
    }

    //Add question method - Gets question info and fills using gets
    private void addQuestion(Question question) {
        ContentValues cv = new ContentValues();
        cv.put(QuestionsTable.COLUMN_QUESTION, question.getQuestion());
        cv.put(QuestionsTable.COLUMN_OPTION1, question.getOption1());
        cv.put(QuestionsTable.COLUMN_OPTION2, question.getOption2());
        cv.put(QuestionsTable.COLUMN_OPTION3, question.getOption3());
        cv.put(QuestionsTable.COLUMN_ANSWER_NR, question.getAnswerNr());
        db.insert(QuestionsTable.TABLE_NAME, null, cv);
    }

    //Arraylist for storing all questions
    public ArrayList<Question> getAllQuestions() {   //create list of questions
        ArrayList<Question> questionList = new ArrayList<>(); //create arrayList
        db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + QuestionsTable.TABLE_NAME, null); //selects full content from table in db

        if (c.moveToFirst()) {
            do {
                Question question = new Question();
                question.setQuestion(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_QUESTION)));
                question.setOption1(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION1)));
                question.setOption2(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION2)));
                question.setOption3(c.getString(c.getColumnIndex(QuestionsTable.COLUMN_OPTION3)));
                question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionsTable.COLUMN_ANSWER_NR)));
                questionList.add(question);
            } while (c.moveToNext());
        }

        c.close();
        return questionList;
    }
}