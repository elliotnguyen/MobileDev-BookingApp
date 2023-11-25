package com.example.ticketbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ticketbooking.Dialog.ProgressHelper;
import com.example.ticketbooking.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    Button signup;
    EditText username, password, re_password, email;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        signup = findViewById(R.id.btn_signup);
        username = findViewById(R.id.username_signup_edit);
        password = findViewById(R.id.password_signup_edit);
        re_password = findViewById(R.id.password_reenter_signup_edit);
        email = findViewById(R.id.email_signup_edit);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Username = username.getText().toString();
                String Password = password.getText().toString();
                String RePassword = re_password.getText().toString();
                String Email = email.getText().toString();

                if (TextUtils.isEmpty(Username) || TextUtils.isEmpty(Email) || TextUtils.isEmpty(Password) || TextUtils.isEmpty(RePassword)) {
                    ProgressHelper.dismissDialog();
                    Toast.makeText(SignUpActivity.this, "Please Enter Valid Information", Toast.LENGTH_SHORT).show();
                } else if (!Email.matches(emailPattern)) {
                    ProgressHelper.dismissDialog();
                    email.setError("Type a valid email here");
                } else if (Password.length() < 6) {
                    ProgressHelper.dismissDialog();
                    password.setError("Password must be 6 characters or more");
                } else if (!Password.equals(RePassword)) {
                    ProgressHelper.dismissDialog();
                    re_password.setError("The password don't match");
                } else {
                    auth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String id = task.getResult().getUser().getUid();
                                DatabaseReference reference = database.getReference().child("users").child(id);

                                String imageUri = "https://firebasestorage.googleapis.com/v0/b/chatapplication-5a305.appspot.com/o/profile.png?alt=media&token=5d39d447-aa83-46dd-a480-cc5820227bc8&_gl=1*8hvs1*_ga*MTQ2MzIyNDc3NS4xNjk3MjExODc4*_ga_CW55HF8NVT*MTY5NzI2NDk0OS41LjEuMTY5NzI2NjQ1NC4xMi4wLjA";
                                User user = new User(imageUri,id, Email, Username, Password);
                                reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            ProgressHelper.showDialog(SignUpActivity.this, "Establishing The Account...");
                                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(SignUpActivity.this, "Error in creating the user", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        ProgressHelper.dismissDialog();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ProgressHelper.dismissDialog();
    }
}