package com.example.doctorappointment.ui.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorappointment.DatabaseHelper;
import com.example.doctorappointment.R;
import com.example.doctorappointment.adapters.AppointmentAdapter;
import com.example.doctorappointment.models.Appointment;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    private TextView totalUsersTextView, totalDoctorsTextView, totalAppointmentsTextView;
    private RecyclerView pendingAppointmentsRecyclerView;
    private Button addDoctorButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);

        // Initialize views
        totalUsersTextView = findViewById(R.id.total_users_text_view);
        totalDoctorsTextView = findViewById(R.id.total_doctors_text_view);
        totalAppointmentsTextView = findViewById(R.id.total_appointments_text_view);
        pendingAppointmentsRecyclerView = findViewById(R.id.pending_appointments_recycler_view);
        addDoctorButton = findViewById(R.id.add_doctor_button);

        // Set up RecyclerView
        pendingAppointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Load dashboard data
        loadDashboardData();
        
        // Set up add doctor button
        addDoctorButton.setOnClickListener(v -> {
            // Show add doctor dialog
            Toast.makeText(this, "Add doctor functionality coming soon", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadDashboardData() {
        // TODO: Implement dashboard data loading from database
        // For now, just display placeholder data
        totalUsersTextView.setText("Total Users: 25");
        totalDoctorsTextView.setText("Total Doctors: 5");
        totalAppointmentsTextView.setText("Total Appointments: 12");
        
        // Set up pending appointments (placeholder)
        pendingAppointmentsRecyclerView.setAdapter(new AppointmentAdapter());
    }
}