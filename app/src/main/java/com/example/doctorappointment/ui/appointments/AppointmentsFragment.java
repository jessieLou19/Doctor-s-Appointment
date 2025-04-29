package com.example.doctorappointment.ui.appointments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.doctorappointment.DatabaseHelper;
import com.example.doctorappointment.adapters.AppointmentAdapter;
import com.example.doctorappointment.databinding.FragmentAppointmentsBinding;
import com.example.doctorappointment.models.Appointment;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;

public class AppointmentsFragment extends Fragment implements BookAppointmentDialog.OnAppointmentBookedListener {
    private FragmentAppointmentsBinding binding;
    private DatabaseHelper dbHelper;
    private AppointmentAdapter appointmentAdapter;
    private boolean showingUpcoming = true;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        binding = FragmentAppointmentsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DatabaseHelper(requireContext());
        setupUI();
        setupTabs();
        setupAppointmentsList();
    }

    private void setupUI() {
        binding.fabAddAppointment.setOnClickListener(v -> showBookAppointmentDialog());
    }

    private void setupTabs() {
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Upcoming"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Past"));
        
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showingUpcoming = tab.getPosition() == 0;
                loadAppointments(showingUpcoming);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupAppointmentsList() {
        appointmentAdapter = new AppointmentAdapter();
        appointmentAdapter.setOnAppointmentClickListener(new AppointmentAdapter.OnAppointmentClickListener() {
            @Override
            public void onAppointmentClick(Appointment appointment) {
                // Show appointment details
            }

            @Override
            public void onRescheduleClick(Appointment appointment) {
                // Show reschedule dialog
            }

            @Override
            public void onCancelClick(Appointment appointment) {
                // Show cancel confirmation dialog
            }
        });

        binding.appointmentsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.appointmentsRecyclerView.setAdapter(appointmentAdapter);
        loadAppointments(true);
    }

    private void loadAppointments(boolean upcoming) {
        // TODO: Load appointments from database
        // For now, just show empty list
        appointmentAdapter.setAppointments(new ArrayList<>());
    }

    private void showBookAppointmentDialog() {
        // TODO: Get actual patient ID from shared preferences or user session
        long patientId = 1; // Temporary hardcoded value
        BookAppointmentDialog dialog = new BookAppointmentDialog(requireContext(), patientId, this);
        dialog.show();
    }

    @Override
    public void onAppointmentBooked() {
        // Refresh the appointments list
        loadAppointments(showingUpcoming);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
} 