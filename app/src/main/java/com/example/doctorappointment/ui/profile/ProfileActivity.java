package com.example.doctorappointment.ui.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.doctorappointment.DatabaseHelper;
import com.example.doctorappointment.R;
import com.example.doctorappointment.models.User;

public class ProfileActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, phoneEditText;
    private Button saveButton;
    private TextView notificationsTextView;
    private DatabaseHelper dbHelper;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Get user ID from intent
        userId = getIntent().getLongExtra("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);

        // Initialize views
        nameEditText = findViewById(R.id.name_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        saveButton = findViewById(R.id.save_button);
        notificationsTextView = findViewById(R.id.notifications_text_view);

        // Load user data
        loadUserData();
        
        // Load notifications
        loadNotifications();

        // Set up save button
        saveButton.setOnClickListener(v -> saveUserData());
    }

    private void loadUserData() {
        // TODO: Implement user data loading from database
        // For now, just display placeholder data
        nameEditText.setText("John Doe");
        emailEditText.setText("john.doe@example.com");
        phoneEditText.setText("123-456-7890");
    }
    
    private void loadNotifications() {
        // Get notifications from database
        int unreadCount = dbHelper.getUnreadNotificationCount(userId);
        
        if (unreadCount > 0) {
            notificationsTextView.setText("You have " + unreadCount + " unread notifications");
            notificationsTextView.setVisibility(View.VISIBLE);
        } else {
            notificationsTextView.setVisibility(View.GONE);
        }
    }

    private void saveUserData() {
        // TODO: Implement user data saving to database
        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
    }
}