package com.example.doctorappointment.ui.appointments;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class AppointmentsActivity extends AppCompatActivity implements AppointmentAdapter.OnAppointmentClickListener {

    private RecyclerView appointmentsRecyclerView;
    private AppointmentAdapter appointmentAdapter;
    private TextView emptyView;
    private FloatingActionButton addAppointmentButton;
    private DatabaseHelper dbHelper;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);

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
        appointmentsRecyclerView = findViewById(R.id.appointments_recycler_view);
        emptyView = findViewById(R.id.empty_view);
        addAppointmentButton = findViewById(R.id.add_appointment_button);

        // Set up RecyclerView
        appointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        appointmentAdapter = new AppointmentAdapter();
        appointmentAdapter.setOnAppointmentClickListener(this);
        appointmentsRecyclerView.setAdapter(appointmentAdapter);

        // Set up add appointment button
        addAppointmentButton.setOnClickListener(v -> {
            BookAppointmentDialog dialog = new BookAppointmentDialog(this, userId);
            dialog.setOnAppointmentBookedListener(() -> loadAppointments());
            dialog.show();
        });

        // Load appointments
        loadAppointments();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAppointments();
    }

    private void loadAppointments() {
        List<Appointment> appointments = dbHelper.getPatientAppointments(userId);
        
        if (appointments.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
            appointmentsRecyclerView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.GONE);
            appointmentsRecyclerView.setVisibility(View.VISIBLE);
            appointmentAdapter.setAppointments(appointments);
        }
    }

    @Override
    public void onAppointmentClick(Appointment appointment) {
        // Show appointment details
        Toast.makeText(this, "Appointment details", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRescheduleClick(Appointment appointment) {
        // Show reschedule dialog
        BookAppointmentDialog dialog = new BookAppointmentDialog(this, userId, appointment.getDoctorId());
        dialog.setOnAppointmentBookedListener(() -> {
            // Cancel old appointment
            dbHelper.updateAppointmentStatus(appointment.getId(), "CANCELLED");
            loadAppointments();
        });
        dialog.show();
    }

    @Override
    public void onCancelClick(Appointment appointment) {
        // Cancel appointment
        if (dbHelper.updateAppointmentStatus(appointment.getId(), "CANCELLED")) {
            Toast.makeText(this, "Appointment cancelled", Toast.LENGTH_SHORT).show();
            loadAppointments();
        } else {
            Toast.makeText(this, "Failed to cancel appointment", Toast.LENGTH_SHORT).show();
        }
    }
}