package com.example.doctorappointment.ui.appointments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.doctorappointment.R;

public class AppointmentsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Simple implementation to avoid crashes
        return inflater.inflate(R.layout.fragment_appointments, container, false);
    }
} 
