package com.example.home.optometryapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ReadActivity extends AppCompatActivity{
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference bookRef = db.collection("Book");
    private BookAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        setUpRecyclerView();
    }
        private void setUpRecyclerView() {
            //orders chapter by ascending
            Query query = bookRef.orderBy("chapterNumber", Query.Direction.ASCENDING);

            //create recyclerOptions for book class
            //FirestoreRecycler used to get query into adapter
            FirestoreRecyclerOptions<Book> options = new FirestoreRecyclerOptions.Builder<Book>()
                    .setQuery(query, Book.class)
                    .build();

            //assign adapter variable
            adapter = new BookAdapter(options);

            //reference recycler view
            RecyclerView recyclerView = findViewById(R.id.recycler_view);
            //fixed size to improve performance
            recyclerView.setHasFixedSize(true);
            //sets layout manager
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            //pass adapter into recyclerview
            recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //tells adapter to start listening
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //tells adapter to stop listening
        adapter.stopListening();
    }
}


