package com.example.medical_appointments_application.patient;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.medical_appointments_application.R;
import com.example.medical_appointments_application.model.Patient;

public class PatientDetailsFragment extends Fragment {

    private Patient patient;

    public PatientDetailsFragment() {

    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_details, container, false);

        TextView nameTextView = view.findViewById(R.id.nameTextView);
        TextView surnameTextView = view.findViewById(R.id.surnameTextView);
        TextView ageTextView = view.findViewById(R.id.ageTextView);
        TextView telephoneNumberTextView = view.findViewById(R.id.telephoneNumberTextView);

        // Retrieve the patient object from the activity
        Patient patient = ((PatientActivity) requireActivity()).getPatient();

        if (patient != null) {
            nameTextView.setText("Name: " + patient.getName());
            surnameTextView.setText("Surname: " + patient.getSurname());
            ageTextView.setText("Age: " + String.valueOf(patient.getAge()));
            telephoneNumberTextView.setText("Telephone number: " + patient.getTelephoneNumber());
        }

        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}
