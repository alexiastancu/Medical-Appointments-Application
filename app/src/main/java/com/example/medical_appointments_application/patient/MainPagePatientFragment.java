package com.example.medical_appointments_application.patient;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.medical_appointments_application.R;
import com.example.medical_appointments_application.database.AppDatabase;
import com.example.medical_appointments_application.database.AppointmentDao;
import com.example.medical_appointments_application.database.DoctorDao;
import com.example.medical_appointments_application.database.PatientDao;
import com.example.medical_appointments_application.model.Appointment;
import com.example.medical_appointments_application.model.AppointmentAdapter;
import com.example.medical_appointments_application.model.Patient;

import java.util.ArrayList;
import java.util.List;

public class MainPagePatientFragment extends Fragment {

    private AppointmentDao appointmentDao;
    private PatientDao patientDao;

    RecyclerView recyclerView;

    private Patient patient;
    private AppointmentAdapter appointmentAdapter;

    public MainPagePatientFragment() {
        // Required empty public constructor
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_page_patient, container, false);

        recyclerView = view.findViewById(R.id.appointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        appointmentDao = AppDatabase.getInstance(requireContext()).appointmentDao();
        patientDao = AppDatabase.getInstance(requireContext()).patientDao();
        appointmentAdapter = new AppointmentAdapter(requireContext(), appointmentDao);

        patient = ((PatientActivity) requireActivity()).getPatient();

        new AsyncTask<Void, Void, List<Appointment>>() {
            @Override
            protected List<Appointment> doInBackground(Void... voids) {
                if (patient != null) {
                    return appointmentDao.getAppointmentsForPatient(patient.getId());
                }
                return new ArrayList<>();
            }

            @Override
            protected void onPostExecute(List<Appointment> appointments) {
                super.onPostExecute(appointments);
                appointmentAdapter.setAppointments(appointments);
                recyclerView.setAdapter(appointmentAdapter);
            }
        }.execute();

        return view;
    }

    @SuppressLint("StaticFieldLeak")
    public void refreshUI() {
        new AsyncTask<Void, Void, List<Appointment>>() {
            @Override
            protected List<Appointment> doInBackground(Void... voids) {
                if (patient != null) {
                    return appointmentDao.getAppointmentsForPatient(patient.getId());
                }
                return new ArrayList<>();
            }

            @Override
            protected void onPostExecute(List<Appointment> appointments) {
                super.onPostExecute(appointments);
                appointmentAdapter.setAppointments(appointments);
                recyclerView.setAdapter(appointmentAdapter);
                appointmentAdapter.notifyDataSetChanged();
            }
        }.execute();
    }


}
