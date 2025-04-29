package com.example.doctorappointment;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.doctorappointment.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize the database
        setupDatabase();
        
        // Setup navigation
        setupNavigation();
    }

    private void setupDatabase() {
        // Initialize database helper
        // DatabaseHelper dbHelper = new DatabaseHelper(this);
    }

    private void setupNavigation() {
        // Setup bottom navigation or drawer navigation
        // Will be implemented later
    }
}