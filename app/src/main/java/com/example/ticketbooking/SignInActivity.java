package com.example.ticketbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ticketbooking.dialog.ProgressHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    Button login;
    EditText email, password;
    TextView signup;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        login = findViewById(R.id.btn_login);
        email = findViewById(R.id.email_login_edit);
        password = findViewById(R.id.password_login_edit);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressHelper.showDialog(SignInActivity.this, "Please Wait...");
                handleLogin();
            }
        });

        signup = findViewById(R.id.convert_to_sign_up);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this , SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void handleLogin() {
        String Email = email.getText().toString();
        String Password = password.getText().toString();

        if (TextUtils.isEmpty(Email)) {
            ProgressHelper.dismissDialog();
            Toast.makeText(SignInActivity.this, "Enter The Email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Password)) {
            ProgressHelper.dismissDialog();
            Toast.makeText(SignInActivity.this, "Enter The Password", Toast.LENGTH_SHORT).show();
        } else if (!Email.matches(emailPattern)) {
            ProgressHelper.dismissDialog();
            email.setError("Give Proper Email Address");
        } else if (Password.length() < 6) {
            ProgressHelper.dismissDialog();
            password.setError("More Then Six Characters");
            Toast.makeText(SignInActivity.this, "Password Needs To Be Longer Then Six Characters", Toast.LENGTH_SHORT).show();
        } else {
            auth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        ProgressHelper.showDialog(SignInActivity.this, "Please Wait...");
                        try {
                            Intent intent = new Intent(SignInActivity.this , MainActivity.class);
                            startActivity(intent);
                            finish();
                        }catch (Exception e){
                            Toast.makeText(SignInActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
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