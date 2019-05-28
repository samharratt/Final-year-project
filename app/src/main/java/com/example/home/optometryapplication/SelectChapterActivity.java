package com.example.home.optometryapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class SelectChapterActivity extends AppCompatActivity {

   //Initialising cardview items
    CardView chapter1, chapter2, chapter3, chapter4, wholeBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_chapter);

        chapter1 = findViewById(R.id.chapter1);
        chapter2 = findViewById(R.id.chapter2);
        chapter3 = findViewById(R.id.chapter3);
        chapter4 = findViewById(R.id.chapter4);
        wholeBook = findViewById(R.id.wholeBook);

        //OnClick for book button
        wholeBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectChapterActivity.this, PdfReadActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        //OnClick for chapter 1
        chapter1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectChapterActivity.this, PdfReadChapter1Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        //OnClick for chapter 2
        chapter2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectChapterActivity.this, PdfReadChapter2Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        //OnClick for chapter 3
        chapter3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectChapterActivity.this, PdfReadChapter3Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        //OnClick for chapter 4
        chapter4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectChapterActivity.this, PdfReadChapter4Activity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
    }

}

