package com.example.doctorappointment.ui.appointments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.example.doctorappointment.DatabaseHelper;
import com.example.doctorappointment.R;
import com.example.doctorappointment.databinding.DialogBookAppointmentBinding;
import com.google.android.material.textfield.TextInputEditText;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BookAppointmentDialog extends Dialog {
    private DialogBookAppointmentBinding binding;
    private final DatabaseHelper dbHelper;
    private final long patientId;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private final SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private DatabaseHelper.Doctor selectedDoctor;
    private Calendar selectedDateTime = Calendar.getInstance();

    public interface OnAppointmentBookedListener {
        void onAppointmentBooked();
    }

    private OnAppointmentBookedListener listener;

    public BookAppointmentDialog(@NonNull Context context, long patientId, OnAppointmentBookedListener listener) {
        super(context);
        this.dbHelper = new DatabaseHelper(context);
        this.patientId = patientId;
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogBookAppointmentBinding.inflate(LayoutInflater.from(getContext()));
        setContentView(binding.getRoot());

        setupDoctorSpinner();
        setupDatePicker();
        setupTimePicker();
        setupButtons();
    }

    private void setupDoctorSpinner() {
        List<DatabaseHelper.Doctor> doctors = dbHelper.getAllDoctors();
        ArrayAdapter<DatabaseHelper.Doctor> adapter = new ArrayAdapter<>(
            getContext(),
            android.R.layout.simple_dropdown_item_1line,
            doctors
        );
        
        AutoCompleteTextView doctorSpinner = binding.doctorSpinner;
        doctorSpinner.setAdapter(adapter);
        doctorSpinner.setOnItemClickListener((parent, view, position, id) -> {
            selectedDoctor = (DatabaseHelper.Doctor) parent.getItemAtPosition(position);
        });
    }

    private void setupDatePicker() {
        TextInputEditText datePickerEditText = binding.datePickerEditText;
        datePickerEditText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, year, month, dayOfMonth) -> {
                    selectedDateTime.set(Calendar.YEAR, year);
                    selectedDateTime.set(Calendar.MONTH, month);
                    selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    datePickerEditText.setText(dateFormatter.format(selectedDateTime.getTime()));
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            );
            
            // Set minimum date to today
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });
    }

    private void setupTimePicker() {
        TextInputEditText timePickerEditText = binding.timePickerEditText;
        timePickerEditText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                (view, hourOfDay, minute) -> {
                    selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    selectedDateTime.set(Calendar.MINUTE, minute);
                    timePickerEditText.setText(timeFormatter.format(selectedDateTime.getTime()));
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            );
            timePickerDialog.show();
        });
    }

    private void setupButtons() {
        binding.cancelButton.setOnClickListener(v -> dismiss());
        binding.bookButton.setOnClickListener(v -> attemptBooking());
    }

    private void attemptBooking() {
        if (selectedDoctor == null) {
            Toast.makeText(getContext(), "Please select a doctor", Toast.LENGTH_SHORT).show();
            return;
        }

        String date = binding.datePickerEditText.getText().toString();
        String time = binding.timePickerEditText.getText().toString();
        
        if (date.isEmpty() || time.isEmpty()) {
            Toast.makeText(getContext(), "Please select date and time", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the time slot is available
        if (!dbHelper.isTimeSlotAvailable(selectedDoctor.id, date, time)) {
            Toast.makeText(getContext(), "This time slot is not available", Toast.LENGTH_SHORT).show();
            return;
        }

        // Book the appointment
        String notes = binding.notesEditText.getText().toString();
        long appointmentId = dbHelper.bookAppointment(
            selectedDoctor.id,
            patientId,
            date,
            time,
            notes
        );

        if (appointmentId != -1) {
            Toast.makeText(getContext(), "Appointment booked successfully", Toast.LENGTH_SHORT).show();
            if (listener != null) {
                listener.onAppointmentBooked();
            }
            dismiss();
        } else {
            Toast.makeText(getContext(), "Failed to book appointment", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        binding = null;
    }
} 