package com.example.finalproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    //UI references
    TextView textView;
    EditText EditEmail, EditPW;
    Button loginbtn, createAccount;
    ProgressBar progressBar;


    FirebaseAuth mAuth;
    FirebaseUser user = null;

    View focusView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TextView
        textView = (TextView) findViewById(R.id.textView);

        //EditText
        EditEmail = (EditText) findViewById(R.id.emailID);
        EditPW = (EditText) findViewById(R.id.password);
        //Buttons
        loginbtn = (Button) findViewById(R.id.Login);
        createAccount = (Button) findViewById(R.id.CreateAccount);

        //Progress Bar
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        user = mAuth.getCurrentUser();
    }


    public void Login(View view) {
        //Makes the progress bar visible.
        progressBar.setVisibility(View.VISIBLE);

        String email = EditEmail.getText().toString();
        String password = EditPW.getText().toString();

        //Reset Errors
        EditEmail.setError(null);
        EditPW.setError(null);

        focusView = null;
        Log.d("isEmailValid", isEmailValid(email) + "");
        Log.d("isPasswordValid", isPasswordValid(password) + "");
        if (isEmailValid(email) && isPasswordValid(password)) {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                // Display sign in successful
                                //Toast.makeText(getApplicationContext(), "Authentication Successful.", Toast.LENGTH_SHORT).show();
                                user = mAuth.getCurrentUser();
                                //showProgress(true);

                                toNoteList();

                            } else { // Assume the user put in the wrong password
                                // Empties any password written in.
                                EditPW.setText("");

                                // If sign in fails, display a message to the user.
                                EditPW.setError(getString(R.string.error_incorrect_password));
                                focusView = EditPW;
                                focusView.requestFocus();
                            }
                        }
                    });
        } else {
            if(!isPasswordValid(password)) {
                focusView = EditPW;
                EditPW.setText("");
                EditPW.setError(getString(R.string.error_invalid_password));
            }
            if (!isEmailValid(email)) {
                focusView = EditEmail;
                EditEmail.setText("");
                EditEmail.setError(getString(R.string.error_invalid_email));
                //Toast.makeText(this, "Email is empty", Toast.LENGTH_LONG).show();
            }

            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
    }

    public void CreateAccount(View view) {
        //Makes the progress bar visible.
        progressBar.setVisibility(View.VISIBLE);

        String email = EditEmail.getText().toString();
        String password = EditPW.getText().toString();

        focusView = null;
        if (isEmailValid(email) && isPasswordValid(password)) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                // Display sign in successful
                                Toast.makeText(getApplicationContext(), "Created Account", Toast.LENGTH_SHORT).show();
                                user = mAuth.getCurrentUser();
                                //showProgress(true);
                                toNoteList();

                            } else {
                                // If sign in fails, display a message to the user.
                                EditEmail.setError(getString(R.string.error_email_exists));
                                focusView = EditEmail;
                                focusView.requestFocus();
                            }
                        }
                    });
        } else {
            if(!isPasswordValid(password)) {
                focusView = EditPW;
                EditPW.setText("");
                EditPW.setError(getString(R.string.error_invalid_password));
            }
            if (!isEmailValid(email)) {
                focusView = EditEmail;
                EditEmail.setText("");
                EditEmail.setError(getString(R.string.error_invalid_email));
                //Toast.makeText(this, "Email is empty", Toast.LENGTH_LONG).show();
            }

            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }

    }

    private boolean isEmailValid(String email) {
        //Email Requirements
        return email.contains("@") && email.contains(".") && !email.isEmpty();
    }

    private boolean isPasswordValid(String password) {
        /*Password Requirements are enforce using a regex
            Minimum eight characters in length .{8,}
            At least one upper case letter (?=.*?[A-Z])
            At least one lowercase letter (?=.*?[a-z])
            and at least one digit (?=.*?[0-9]).    */

        Pattern pattern = Pattern.compile("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9]).{8,}");

        // Checks if the password matches the requirements.
        return pattern.matcher(password).matches();
    }


    public void toNoteList() {
        Intent switchToNoteList= new Intent(this, NoteListActivity.class);
        startActivity(switchToNoteList);
    }




}