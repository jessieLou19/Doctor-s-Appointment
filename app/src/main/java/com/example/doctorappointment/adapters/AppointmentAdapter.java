package com.example.doctorappointment.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doctorappointment.R;
import com.example.doctorappointment.models.Appointment;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {
    private List<Appointment> appointments = new ArrayList<>();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
    private OnAppointmentClickListener listener;

    public interface OnAppointmentClickListener {
        void onAppointmentClick(Appointment appointment);
        void onRescheduleClick(Appointment appointment);
        void onCancelClick(Appointment appointment);
    }

    public void setOnAppointmentClickListener(OnAppointmentClickListener listener) {
        this.listener = listener;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        if (position < appointments.size()) {
            holder.bind(appointments.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    class AppointmentViewHolder extends RecyclerView.ViewHolder {
        private final TextView appointmentDateTime;
        private final TextView doctorName;
        private final TextView appointmentStatus;
        private final MaterialButton rescheduleButton;
        private final MaterialButton cancelButton;

        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            appointmentDateTime = itemView.findViewById(R.id.appointmentDateTime);
            doctorName = itemView.findViewById(R.id.doctorName);
            appointmentStatus = itemView.findViewById(R.id.appointmentStatus);
            rescheduleButton = itemView.findViewById(R.id.rescheduleButton);
            cancelButton = itemView.findViewById(R.id.cancelButton);
        }

        void bind(final Appointment appointment) {
            // Format the date and time
            String dateTimeString = dateFormat.format(appointment.getDate()) + " " + appointment.getTime();
            appointmentDateTime.setText(dateTimeString);
            
            // TODO: Get doctor name from database using appointment.getDoctorId()
            doctorName.setText("Dr. Smith"); // Placeholder
            appointmentStatus.setText(appointment.getStatus());

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onAppointmentClick(appointment);
            });

            rescheduleButton.setOnClickListener(v -> {
                if (listener != null) listener.onRescheduleClick(appointment);
            });

            cancelButton.setOnClickListener(v -> {
                if (listener != null) listener.onCancelClick(appointment);
            });
        }
    }
} 
