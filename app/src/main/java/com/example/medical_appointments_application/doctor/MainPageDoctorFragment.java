package com.example.medical_appointments_application.doctor;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medical_appointments_application.R;
import com.example.medical_appointments_application.database.AppDatabase;
import com.example.medical_appointments_application.database.AppointmentDao;
import com.example.medical_appointments_application.model.Appointment;
import com.example.medical_appointments_application.model.AppointmentAdapter;

import java.util.List;


public class MainPageDoctorFragment extends Fragment {

    private AppointmentDao appointmentDao;
    private AppointmentAdapter appointmentAdapter;

    public MainPageDoctorFragment() {
        // Required empty public constructor
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_page_doctor, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.appointments);

        // Set the layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Initialize the appointmentDao
        appointmentDao = AppDatabase.getInstance(requireContext()).appointmentDao();
        appointmentAdapter = new AppointmentAdapter(appointmentDao);

        new AsyncTask<Void, Void, List<Appointment>>() {
            @Override
            protected List<Appointment> doInBackground(Void... voids) {
                return appointmentDao.getAllAppointments();
            }

            @Override
            protected void onPostExecute(List<Appointment> appointments) {
                appointments.add(new Appointment(1, 1, "15/06/2023", "15:00", "Spitalul Regina Maria", "Cardiologie"));
                appointmentAdapter.setAppointments(appointments);
                recyclerView.setAdapter(appointmentAdapter);
            }
        }.execute();
        return view;
    }
}
