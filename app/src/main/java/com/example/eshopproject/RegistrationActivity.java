package com.example.eshopproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.os.Bundle;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class RegistrationActivity extends AppCompatActivity {
    private EditText  emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        // Initialize views
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button registerButton = findViewById(R.id.registerButton);
        Button login = findViewById(R.id.LogInButton);

        // Handle registerButton click
        registerButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            // Save user registration data to Firebase Firestore collection
            saveUserData(email, password);
        });
        // Handle LogIn click
        login.setOnClickListener(v -> {
            startActivity(new Intent(RegistrationActivity.this, LogInActivity.class));
            finish();
        });
    }
    private void saveUserData( String email, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .build();
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(profileTask -> {
                                    if (profileTask.isSuccessful()) {
                                        // User registration successful
                                        Toast.makeText(RegistrationActivity.this, "Registration succeeded", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(RegistrationActivity.this, LogInActivity.class));
                                        finish();
                                    } else {
                                        // User registration failed
                                        Toast.makeText(RegistrationActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        // User registration failed
                        Toast.makeText(RegistrationActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
