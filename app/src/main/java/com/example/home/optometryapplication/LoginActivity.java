package com.example.home.optometryapplication;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements OnClickListener {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    // UI references.
    private FirebaseAuth firebaseAuth;
    private AutoCompleteTextView editTextEmailView;
    private EditText editTextPasswordView;
    private Button mEmailSignInButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //getting firebase auth object
        firebaseAuth = FirebaseAuth.getInstance();

        //checks if user is logged in
        if (firebaseAuth.getCurrentUser() != null) {
            //close this activity
            finish();
            //opening admin activity
            startActivity(new Intent(getApplicationContext(), AdminActivity.class));
        }

        // Set up the login form.
        editTextEmailView = findViewById(R.id.inputEmail);
        editTextPasswordView = findViewById(R.id.inputPassword);
        mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(this);
    }

    //OnClick for userLogin method
    @Override
    public void onClick(View v) {
        if (v == mEmailSignInButton) {
            userLogin();
        }
    }

    //method for Firebase Authentication to login
    private void userLogin() {
        // Store values at the time of the login attempt.
        String email = editTextEmailView.getText().toString();
        String password = editTextPasswordView.getText().toString();
        //Checks if email input is bank
        if (email.isEmpty()) {
            editTextEmailView.setError("Email is required");
            editTextEmailView.requestFocus();
            return;
        }
        //Checks if password input is blank
        if (password.isEmpty()) {
            editTextPasswordView.setError("Password is required");
            editTextPasswordView.requestFocus();
            return;
        }
        //Checks if there is a valid email address
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmailView.setError("Please enter valid email address");
            editTextEmailView.requestFocus();
            return;
        }
        //Checks if password is over 6 characters
        if (password.length() < 6) {
            editTextPasswordView.setError("Minimum length of password is 6");
            editTextPasswordView.requestFocus();
            return;
        }
        //Firebase login
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //Clears all activities on top
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

        //add email to email auto complete
        private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
            //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
            ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        editTextEmailView.setAdapter(adapter);
    }
}


