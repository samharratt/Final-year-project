package com.example.home.optometryapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

public class ChooseBookActivity extends AppCompatActivity {

    //Initialise CardView
    CardView rl_pdf, rl_new;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_book);


        rl_pdf = findViewById(R.id.pdfBook);
        rl_new = findViewById(R.id.updatedBook);
        //OnClickListener for changing to the ViewPDF activity
        rl_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseBookActivity.this, SelectChapterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });
        //OnClickListener for changing to the Read activity
        rl_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChooseBookActivity.this, ReadActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

    }
}
