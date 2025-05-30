package com.example.firstproject.beathub20;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class registerpage extends AppCompatActivity {

    TextView alreadylogin, signUsername, signpassword, signRegister, signemail, signphone;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registerpage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        alreadylogin = findViewById(R.id.alreadylogin);
        signUsername = findViewById(R.id.Username);
        signpassword = findViewById(R.id.password);
        signRegister = findViewById(R.id.Register);
        signemail = findViewById(R.id.email);
        signphone = findViewById(R.id.number);

        mAuth = FirebaseAuth.getInstance();

        signRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = signUsername.getText().toString().trim();
                String password = signpassword.getText().toString().trim();
                String email = signemail.getText().toString().trim();
                String phone = signphone.getText().toString().trim();

                if(email.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty()) {
                    // Handle empty inputs (optional)
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if(task.isSuccessful()) {
                                // Save additional info in Realtime Database
                                database = FirebaseDatabase.getInstance();
                                reference = database.getReference("user");

                                helperClass helperClass = new helperClass(name, password, email, phone);
                                reference.child(name).setValue(helperClass);

                                Toast.makeText(registerpage.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(registerpage.this, loginpage.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(registerpage.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        alreadylogin.setOnClickListener(v -> {
            Intent i = new Intent(registerpage.this, loginpage.class);
            startActivity(i);
        });
    }
}
