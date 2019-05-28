package com.example.home.optometryapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

import javax.annotation.Nullable;

public class AdminUpdateActivity extends AppCompatActivity {

    private static final String TAG = AdminUpdateActivity.class.getName();  //declared tag for printing errors
    private FirebaseFirestore db = FirebaseFirestore.getInstance();     //gets Firestore instance.
    private CollectionReference bookRef = db.collection("Book");    //gets CollectionReference
    private BookAdapter adapter;    //Adapter for the book


    //variables
    private EditText editTextChapterName;
    private EditText editTextChapterInfo;
    private NumberPicker numberPickerChapterNumber;

    //Declarating the inputs
    String mChapterName;
    String mChapterInfo;
    int mChapterNumber;
    String mMyID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update);

        //Changes back arrow to close icon and sets the title in the actionbar to "Add Chapter"
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close);
        setTitle("Update Chapter");

        //initialisation
        editTextChapterName = findViewById(R.id.edit_text_chapter_name);
        editTextChapterInfo = findViewById(R.id.edit_text_chapter_info);
        numberPickerChapterNumber = findViewById(R.id.number_picker_chapter_number);
        numberPickerChapterNumber.setMinValue(1);
        numberPickerChapterNumber.setMaxValue(25);
        //get data
        Bundle bundle = getIntent().getExtras();
        mChapterName = bundle.getString("mChapterName");
        mChapterInfo = bundle.getString("mChapterInfo");
        mChapterNumber = bundle.getInt("mChapterNumber");
        mMyID = bundle.getString("mMyID");
        //set data
        editTextChapterName.setText(mChapterName);
        editTextChapterInfo.setText(mChapterInfo);
        numberPickerChapterNumber.setValue(mChapterNumber);
    }

    //OnCreate inflate the menu with the save icon
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //uses my custom menu instead of regular menu
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_menusaveonly, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //Case statement for calling updateBook method
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_icon:
                updateBook();
                Intent intent = new Intent(AdminUpdateActivity.this, AdminReadActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    //Method for updating book, this updates name,info,and number within Firestore
    private void updateBook() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String id = getIntent().getStringExtra("mMyId");
        String chapterName = editTextChapterName.getText().toString().trim();
        String chapterInfo = editTextChapterInfo.getText().toString().trim();
        int chapterNumber = numberPickerChapterNumber.getValue();

        if (chapterName.trim().isEmpty() || chapterInfo.trim().isEmpty()) { //ensure that user has not left boxes empty
            Toast.makeText(this, "Please add a chapter name and the chapter information", Toast.LENGTH_SHORT).show();
            return;
        }

        DocumentReference bookRef = db
                .collection("Book")
                .document(id);
        bookRef
                .update("chapterName", chapterName, "chapterInfo", chapterInfo, "chapterNumber", chapterNumber)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error", e);
                    }
                });
    }
}
    







