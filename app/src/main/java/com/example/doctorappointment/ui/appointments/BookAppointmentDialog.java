package com.example.doctorappointment.ui.appointments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.example.doctorappointment.DatabaseHelper;
import com.example.doctorappointment.R;
import com.example.doctorappointment.models.Doctor;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BookAppointmentDialog {
    private final Context context;
    private final long patientId;
    private Long selectedDoctorId;
    private final Calendar selectedDateTime = Calendar.getInstance();
    private final DatabaseHelper dbHelper;
    private AlertDialog dialog;
    private OnAppointmentBookedListener listener;

    // Views
    private MaterialAutoCompleteTextView doctorSpinner;
    private TextInputEditText datePickerEditText;
    private TextInputEditText timePickerEditText;
    private TextInputEditText notesEditText;
    private MaterialButton bookButton;
    private MaterialButton cancelButton;

    public interface OnAppointmentBookedListener {
        void onAppointmentBooked();
    }

    public BookAppointmentDialog(Context context, long patientId) {
        this.context = context;
        this.patientId = patientId;
        this.dbHelper = new DatabaseHelper(context);
    }

    public BookAppointmentDialog(Context context, long patientId, long doctorId) {
        this.context = context;
        this.patientId = patientId;
        this.selectedDoctorId = doctorId;
        this.dbHelper = new DatabaseHelper(context);
    }

    public void setOnAppointmentBookedListener(OnAppointmentBookedListener listener) {
        this.listener = listener;
    }

    public void show() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_book_appointment, null);
        
        // Initialize views
        doctorSpinner = view.findViewById(R.id.doctorSpinner);
        datePickerEditText = view.findViewById(R.id.datePickerEditText);
        timePickerEditText = view.findViewById(R.id.timePickerEditText);
        notesEditText = view.findViewById(R.id.notesEditText);
        bookButton = view.findViewById(R.id.bookButton);
        cancelButton = view.findViewById(R.id.cancelButton);
        
        // Set up doctor spinner
        setupDoctorSpinner();
        
        // Set up date picker
        datePickerEditText.setOnClickListener(v -> showDatePicker());
        
        // Set up time picker
        timePickerEditText.setOnClickListener(v -> showTimePicker());
        
        // Set up buttons
        bookButton.setOnClickListener(v -> bookAppointment());
        cancelButton.setOnClickListener(v -> dialog.dismiss());
        
        // Create and show dialog
        dialog = new AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(true)
                .create();
        
        dialog.show();
    }
    
    private void setupDoctorSpinner() {
        List<Doctor> doctors = dbHelper.getAllDoctors();
        
        if (doctors.isEmpty()) {
            // Add sample doctors if none exist
            doctors.add(new Doctor(1, "Dr. Maria Santos", "Cardiology", "Available"));
            doctors.add(new Doctor(2, "Dr. Juan Cruz", "Pediatrics", "Available"));
            doctors.add(new Doctor(3, "Dr. Ana Reyes", "Dermatology", "Available"));
        }
        
        // Create adapter
        ArrayAdapter<Doctor> adapter = new ArrayAdapter<>(
                context, android.R.layout.simple_spinner_item, doctors);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        doctorSpinner.setAdapter(adapter);
        
        // Set selected doctor if provided
        if (selectedDoctorId != null) {
            for (int i = 0; i < doctors.size(); i++) {
                if (doctors.get(i).getId() == selectedDoctorId) {
                    doctorSpinner.setText(doctors.get(i).toString(), false);
                    break;
                }
            }
        }
    }
    
    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                (view, year, month, dayOfMonth) -> {
                    selectedDateTime.set(Calendar.YEAR, year);
                    selectedDateTime.set(Calendar.MONTH, month);
                    selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    updateDateEditText();
                },
                selectedDateTime.get(Calendar.YEAR),
                selectedDateTime.get(Calendar.MONTH),
                selectedDateTime.get(Calendar.DAY_OF_MONTH)
        );
        
        // Set min date to today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        
        datePickerDialog.show();
    }
    
    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                context,
                (view, hourOfDay, minute) -> {
                    selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedDateTime.set(Calendar.MINUTE, minute);
                    updateTimeEditText();
                },
                selectedDateTime.get(Calendar.HOUR_OF_DAY),
                selectedDateTime.get(Calendar.MINUTE),
                false
        );
        
        timePickerDialog.show();
    }

    private void updateDateEditText() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        datePickerEditText.setText(dateFormat.format(selectedDateTime.getTime()));
    }

    private void updateTimeEditText() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
        timePickerEditText.setText(timeFormat.format(selectedDateTime.getTime()));
    }

    private void bookAppointment() {
        // Validate inputs
        if (doctorSpinner.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please select a doctor", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (datePickerEditText.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if (timePickerEditText.getText().toString().isEmpty()) {
            Toast.makeText(context, "Please select a time", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Get selected doctor
        List<Doctor> doctors = dbHelper.getAllDoctors();
        Doctor selectedDoctor = null;
        String selectedDoctorText = doctorSpinner.getText().toString();
        
        for (Doctor doctor : doctors) {
            if (doctor.toString().equals(selectedDoctorText)) {
                selectedDoctor = doctor;
                break;
            }
        }
        
        if (selectedDoctor == null) {
            Toast.makeText(context, "Invalid doctor selection", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Format date for database
        SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dateStr = dbDateFormat.format(selectedDateTime.getTime());
        
        // Format time for database
        SimpleDateFormat dbTimeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String timeStr = dbTimeFormat.format(selectedDateTime.getTime());
        
        // Get notes
        String notes = notesEditText.getText().toString();
        
        // Book appointment
        long appointmentId = dbHelper.bookAppointment(
                patientId,
                selectedDoctor.getId(),
                dateStr,
                timeStr,
                "SCHEDULED",
                notes
        );
        
        if (appointmentId != -1) {
            Toast.makeText(context, "Appointment booked successfully", Toast.LENGTH_SHORT).show();
            if (listener != null) {
                listener.onAppointmentBooked();
            }
            dialog.dismiss();
        } else {
            Toast.makeText(context, "Failed to book appointment", Toast.LENGTH_SHORT).show();
        }
    }
}
