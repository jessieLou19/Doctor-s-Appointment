package com.example.doctorappointment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.doctorappointment.database.DatabaseHelper;
import com.example.doctorappointment.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbHelper = new DatabaseHelper(this);

        setupClickListeners();
    }

    private void setupClickListeners() {
        binding.loginButton.setOnClickListener(v -> attemptLogin());
        binding.registerButton.setOnClickListener(v -> startRegistration());
    }

    private void attemptLogin() {
        String email = binding.emailInput.getText().toString().trim();
        String password = binding.passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show loading state
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.loginButton.setEnabled(false);

        // TODO: Implement actual authentication logic
        // For now, we'll just simulate a successful login
        validateCredentials(email, password);
    }

    private void validateCredentials(String email, String password) {
        // TODO: Replace with actual database validation
        if (email.equals("test@test.com") && password.equals("password")) {
            loginSuccess();
        } else {
            loginFailed();
        }
    }

    private void loginSuccess() {
        binding.progressBar.setVisibility(View.GONE);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish(); // Prevent going back to login screen
    }

    private void loginFailed() {
        binding.progressBar.setVisibility(View.GONE);
        binding.loginButton.setEnabled(true);
        Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
    }

    private void startRegistration() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
} 