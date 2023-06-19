package com.example.medical_appointments_application.doctor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.medical_appointments_application.R;
import com.example.medical_appointments_application.model.Doctor;

public class DoctorDetailsFragment extends Fragment {

    private Doctor doctor;

    public DoctorDetailsFragment() {

    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doctor_details, container, false);

        TextView nameTextView = view.findViewById(R.id.nameTextView);
        TextView surnameTextView = view.findViewById(R.id.surnameTextView);
        TextView specializationTextView = view.findViewById(R.id.specializationTextView);
        TextView emailTextView = view.findViewById(R.id.emailTextView);

        // Retrieve the doctor object from the activity
        Doctor doctor = ((DoctorActivity) requireActivity()).getDoctor();

        if (doctor != null) {
            nameTextView.setText("Name: " + doctor.getName());
            surnameTextView.setText("Surname: " + doctor.getSurname());
            specializationTextView.setText("Specialization: " + doctor.getSpecialization());
            emailTextView.setText("Email: " + doctor.getEmail());
        }

        return view;
    }




    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}