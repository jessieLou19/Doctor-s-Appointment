package com.example.doctorappointment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.doctorappointment.ui.appointments.AppointmentsActivity;
import com.example.doctorappointment.ui.doctors.DoctorsActivity;
import com.example.doctorappointment.ui.profile.ProfileActivity;
import com.example.doctorappointment.ui.admin.AdminDashboardActivity;

public class MainActivity extends AppCompatActivity {
    
    private long userId;
    private String userType;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        dbHelper = new DatabaseHelper(this);
        
        // Check if user is logged in
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = prefs.getLong("userId", -1);
        userType = prefs.getString("userType", "patient");
        
        if (userId == -1) {
            // User not logged in, redirect to login
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        
        // Set up welcome message
        TextView welcomeText = findViewById(R.id.welcome_text);
        TextView userGreeting = findViewById(R.id.user_greeting);
        
        String username = dbHelper.getUserName(userId);
        if (username != null) {
            userGreeting.setText("Hello, " + username + "!");
            userGreeting.setVisibility(View.VISIBLE);
        }
        
        // Set up navigation cards
        setupNavigationCards();
        
        // Show/hide admin dashboard based on user type
        CardView adminCard = findViewById(R.id.admin_card);
        if (adminCard != null) {
            adminCard.setVisibility("admin".equals(userType) ? View.VISIBLE : View.GONE);
        }
        
        // Set up logout button
        Button logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(v -> logout());
    }
    
    private void setupNavigationCards() {
        // Profile card
        CardView profileCard = findViewById(R.id.profile_card);
        profileCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });
        
        // Appointments card
        CardView appointmentsCard = findViewById(R.id.appointments_card);
        appointmentsCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AppointmentsActivity.class);
            intent.putExtra("userId", userId);
            startActivity(intent);
        });
        
        // Doctors card
        CardView doctorsCard = findViewById(R.id.doctors_card);
        doctorsCard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DoctorsActivity.class);
            startActivity(intent);
        });
        
        // Admin dashboard card
        CardView adminCard = findViewById(R.id.admin_card);
        if (adminCard != null) {
            adminCard.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, AdminDashboardActivity.class);
                startActivity(intent);
            });
        }
    }
    
    private void logout() {
        // Clear user session
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        prefs.edit().clear().apply();
        
        // Redirect to login
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}

