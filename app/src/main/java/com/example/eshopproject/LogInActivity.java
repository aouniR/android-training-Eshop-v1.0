package com.example.eshopproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {
    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Initialize views
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        Button registerButton = findViewById(R.id.LogIn);
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // Handle LogIn click
        registerButton.setOnClickListener(v -> {
            String Username = email.getText().toString().trim();
            String Password = password.getText().toString().trim();

            //  Verify authentication
            CheckData(Username, Password,auth);
        });
    }

    private void CheckData(String email, String pass,FirebaseAuth authentication) {
        //authentication
        authentication.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // TODO : Generate a token and proceed the authorization restriction
                            // For the moment, we gonna only move to the next activity
                            startActivity(new Intent(LogInActivity.this, ShopActivity.class));
                            finish();

                        } else {
                            // Sign-in failed, handle the error
                            Toast.makeText(LogInActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
