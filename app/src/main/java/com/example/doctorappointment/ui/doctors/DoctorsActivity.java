package com.example.doctorappointment.ui.doctors;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorappointment.DatabaseHelper;
import com.example.doctorappointment.R;
import com.example.doctorappointment.adapters.DoctorAdapter;
import com.example.doctorappointment.models.Doctor;
import com.example.doctorappointment.ui.appointments.BookAppointmentDialog;

import java.util.List;

public class DoctorsActivity extends AppCompatActivity implements DoctorAdapter.OnDoctorClickListener {

    private RecyclerView doctorsRecyclerView;
    private DoctorAdapter doctorAdapter;
    private TextView emptyView;
    private DatabaseHelper dbHelper;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors);

        // Get user ID from shared preferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = prefs.getLong("userId", -1);

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);

        // Initialize views
        doctorsRecyclerView = findViewById(R.id.doctors_recycler_view);
        emptyView = findViewById(R.id.empty_view);

        // Set up RecyclerView
        doctorsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        
        // Load doctors
        loadDoctors();
    }

    private void loadDoctors() {
        List<Doctor> doctors = dbHelper.getAllDoctors();
        
        if (doctors.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            doctorsRecyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            doctorsRecyclerView.setVisibility(View.VISIBLE);
            doctorAdapter = new DoctorAdapter(doctors);
            doctorAdapter.setOnDoctorClickListener(this);
            doctorsRecyclerView.setAdapter(doctorAdapter);
        }
    }

    @Override
    public void onDoctorClick(Doctor doctor) {
        // Show book appointment dialog
        if (userId != -1) {
            BookAppointmentDialog dialog = new BookAppointmentDialog(this, userId, doctor.getId());
            dialog.show();
        }
    }
}