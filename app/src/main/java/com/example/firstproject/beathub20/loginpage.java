package com.example.firstproject.beathub20;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class loginpage extends AppCompatActivity {

    EditText emailInput, passwordInput;
    TextView loginButton, createAccountLink;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_loginpage);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // UI components
        emailInput = findViewById(R.id.emailAddress);
        passwordInput = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginIn);
        createAccountLink = findViewById(R.id.wantToCreate);

        // Login button click
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        // Link to register page
        createAccountLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(loginpage.this, registerpage.class));
            }
        });
    }

    private void loginUser() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Validate
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Enter a valid email");
            emailInput.requestFocus();
            return;
        }

        if (password.isEmpty() || password.length() < 6) {
            passwordInput.setError("Password must be at least 6 characters");
            passwordInput.requestFocus();
            return;
        }

        // Sign in with Firebase
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(loginpage.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(loginpage.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(loginpage.this, HomePage.class)); // Replace with your main page
                            finish();
                        } else {
                            String errorMsg = task.getException() != null ? task.getException().getMessage() : "Login failed";
                            Toast.makeText(loginpage.this, "Error: " + errorMsg, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
