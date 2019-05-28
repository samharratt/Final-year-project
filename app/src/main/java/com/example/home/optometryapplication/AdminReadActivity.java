package com.example.home.optometryapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AdminReadActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference bookRef = db.collection("Book");
    private BookAdapter adapter;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_read);
        //Connect RecyclerView to adapter
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

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            //used for drag and drop. NOT NEEDED
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }
            //direction does not matter
            //viewHolder stores adapter position
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.deleteItem(viewHolder.getAdapterPosition());

            }
        }).attachToRecyclerView(recyclerView); //class for swiping/dragging and dropping within recyclerview

        /*
        OnClickListener for when the admin selects a position within the recyclerview.
        This then populates the edit activity with the selected position's information.
        */

        adapter.setOnItemClickListener(new BookAdapter.OnItemClickListener() {

            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Book book = documentSnapshot.toObject(Book.class);
                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();
                Toast.makeText(AdminReadActivity.this,
                        "Position: " + position + " ID: " + id, Toast.LENGTH_SHORT).show();
                //gets position for relevant strings and integers.
                String chapterName = adapter.getItem(position).getChapterName();
                String chapterInfo = adapter.getItem(position).getChapterInfo();
                Integer chapterNumber = adapter.getItem(position).getChapterNumber();
                String imageUrl = adapter.getItem(position).getImageUrl();

                //changes the activity to update activity
                Intent intent = new Intent(AdminReadActivity.this, AdminUpdateActivity.class);

                //send the information through the activity into the update activity.
                intent.putExtra("mChapterName", chapterName);
                intent.putExtra("mChapterInfo", chapterInfo);
                intent.putExtra("mChapterNumber", chapterNumber);
                intent.putExtra("mMyId", id);
                intent.putExtra("mImageUrl", imageUrl);

                //opens new activity
                startActivity(intent);

       //         String id = documentSnapshot.getId();
       //         DocumentReference bookRef = db.collection("Book").document(id);
       //         bookRef.update("chapterInfo", "example_title");
            }

        });



    }


    //this method inflates the menu with an add button.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //uses my custom menu instead of regular menu
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_menuaddonly, menu);
        return super.onCreateOptionsMenu(menu);
    }
    /* this method acts as an onclicklistener for the menu bar
       when the add icon is selected it opens the add book activity.
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        {
            if (id == R.id.add_icon) {
                Intent intent = new Intent(AdminReadActivity.this, NewBookActivity.class);
                startActivity(intent);
                return true;
            }
        }
        return false;
    }


    //onStart this method tells the adapter to start listening and start providing the info from Firebase.
    @Override
    protected void onStart() {
        super.onStart();
        //tells adapter to start listening
        adapter.startListening();
    }
    //onStop this method tells the adapter to stop listening, this reduce the internet usage when not needed.
    @Override
    protected void onStop() {
        super.onStop();
        //tells adapter to stop listening
        adapter.stopListening();
    }


}
