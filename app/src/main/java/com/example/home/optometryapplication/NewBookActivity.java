package com.example.home.optometryapplication;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class NewBookActivity extends AppCompatActivity {

    //TAG used for error handling
    private static final String TAG = NewBookActivity.class.getName();

    //variables
    private EditText editTextChapterName;
    private EditText editTextChapterInfo;
    private NumberPicker numberPickerChapterNumber;
    private Button buttonChoose;
    private ImageView mImageView;
    StorageReference mStorageRef;
    CollectionReference bookRef;
    private Uri mImageUri;
    private final int PICK_IMAGE_REQUEST = 1; //used to identify image request
    private StorageTask mUploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_book);

        //Changes back arrow to close icon and sets the title in the actionbar to "Add Chapter"
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.close);
        setTitle("Add Chapter");
        //method that finds the view from the layout resource
        buttonChoose = findViewById(R.id.buttonChoose);
        editTextChapterName = findViewById(R.id.edit_text_chapter_name);
        editTextChapterInfo = findViewById(R.id.edit_text_chapter_info);
        numberPickerChapterNumber = findViewById(R.id.number_picker_chapter_number);
        mImageView = findViewById(R.id.imageViewNewBook);
        //Sets number picker to 1-25 max
        numberPickerChapterNumber.setMinValue(1);
        numberPickerChapterNumber.setMaxValue(25);
        //gets Firebase references for where to store the data
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        bookRef = FirebaseFirestore.getInstance().collection("Book");
        //OnClickListener for adding an image
        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
    }
    //method for opening the android image chooser.
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    //Adds image into image view
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(mImageView);
        }
    }
    //inflates menu with save functionality
    @Override
    public boolean onCreateOptionsMenu(Menu menu) { //uses my custom menu instead of regular menu
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_menusaveonly, menu);
        return super.onCreateOptionsMenu(menu);
    }
    //Adds upload method to save icon
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_icon:
                uploadFile();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    //gets the uri file extension
    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    //Not working
//    private void saveBook() {
//        final String chapterName = editTextChapterName.getText().toString();
//        final String chapterInfo = editTextChapterInfo.getText().toString();
//        final int chapterNumber = numberPickerChapterNumber.getValue();
//
//        if (chapterName.trim().isEmpty() || chapterInfo.trim().isEmpty()) { //ensure that user has not left boxes empty
//            Toast.makeText(this, "Please add a chapter name and the chapter information", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (mImageUri != null) {
//            final StorageReference fileReference = mStorageRef.child("." + System.currentTimeMillis());
//
//            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                @Override
//                public void onSuccess(Uri uri) {
//                    Uri downloadUrl = uri;
//                    CollectionReference bookRef = FirebaseFirestore.getInstance().collection("Book");
//                    bookRef.add(new Book(chapterName, chapterInfo, chapterNumber, downloadUrl.toString()));
//                    Toast.makeText(NewBookActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
//                    finish();
//                }
//            });

    /*
    Upload method - this uploads the chapter name, info, number
    and an image to Firestore and Firebase Storage.
    */
    private void uploadFile() {
        final String chapterName = editTextChapterName.getText().toString();
        final String chapterInfo = editTextChapterInfo.getText().toString();
        final int chapterNumber = numberPickerChapterNumber.getValue();
        //Toast for ensuring inputs are not empty
        if (chapterName.trim().isEmpty() || chapterInfo.trim().isEmpty()) { //ensure that user has not left boxes empty
            Toast.makeText(this, "Please add a chapter name, chapter information and an image", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            //OnSuccess Listener is used to getDownloadURIL
            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        private static final String TAG = "Images";
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful());
                        Uri downloadUrl = urlTask.getResult();
                        //Adds relevant information to Firestore and Images to Storage
                        Log.d(TAG, "onSuccess: firebase download url: " + downloadUrl.toString());
                            Toast.makeText(NewBookActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                            CollectionReference bookRef = FirebaseFirestore.getInstance().collection("Book");
                            bookRef.add(new Book(chapterName, chapterInfo, chapterNumber, downloadUrl.toString()));
                            finish();

                        }
                    });
        }
    }
}





