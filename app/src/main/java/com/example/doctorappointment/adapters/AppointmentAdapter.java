package com.example.doctorappointment.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.doctorappointment.R;
import com.example.doctorappointment.models.Appointment;
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

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);
        holder.bind(appointment);
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
        notifyDataSetChanged();
    }

    class AppointmentViewHolder extends RecyclerView.ViewHolder {
        private final TextView dateTimeText;
        private final TextView doctorNameText;
        private final TextView statusText;
        private final View rescheduleButton;
        private final View cancelButton;

        AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTimeText = itemView.findViewById(R.id.appointmentDateTime);
            doctorNameText = itemView.findViewById(R.id.doctorName);
            statusText = itemView.findViewById(R.id.appointmentStatus);
            rescheduleButton = itemView.findViewById(R.id.rescheduleButton);
            cancelButton = itemView.findViewById(R.id.cancelButton);
        }

        void bind(final Appointment appointment) {
            String dateTimeString = dateFormat.format(appointment.getDate()) + " " + appointment.getTime();
            dateTimeText.setText(dateTimeString);
            // TODO: Get doctor name from database using appointment.getDoctorId()
            doctorNameText.setText("Dr. Smith"); // Placeholder
            statusText.setText(appointment.getStatus());

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onAppointmentClick(appointment);
            });

            rescheduleButton.setOnClickListener(v -> {
                if (listener != null) listener.onRescheduleClick(appointment);
            });

            cancelButton.setOnClickListener(v -> {
                if (listener != null) listener.onCancelClick(appointment);
            });

            // Show/hide action buttons based on appointment status
            boolean isUpcoming = "SCHEDULED".equals(appointment.getStatus());
            rescheduleButton.setVisibility(isUpcoming ? View.VISIBLE : View.GONE);
            cancelButton.setVisibility(isUpcoming ? View.VISIBLE : View.GONE);
        }
    }
} 