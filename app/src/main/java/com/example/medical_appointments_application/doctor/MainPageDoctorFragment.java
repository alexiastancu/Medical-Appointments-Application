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
import com.example.medical_appointments_application.database.DoctorDao;
import com.example.medical_appointments_application.model.Appointment;
import com.example.medical_appointments_application.model.AppointmentAdapter;
import com.example.medical_appointments_application.model.Doctor;
import com.example.medical_appointments_application.model.User;

import java.util.ArrayList;
import java.util.List;


public class MainPageDoctorFragment extends Fragment {

    private AppointmentDao appointmentDao;
    private DoctorDao doctorDao;
    private AppointmentAdapter appointmentAdapter;

    public MainPageDoctorFragment() {

    }

    public static MainPageDoctorFragment newInstance(Doctor doctor) {
        MainPageDoctorFragment fragment = new MainPageDoctorFragment();
        Bundle args = new Bundle();
        args.putParcelable("doctor", doctor);
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_page_doctor, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.appointments);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        appointmentDao = AppDatabase.getInstance(requireContext()).appointmentDao();
        doctorDao = AppDatabase.getInstance(requireContext()).doctorDao();
        AppointmentAdapter appointmentAdapter = new AppointmentAdapter(requireContext(), appointmentDao);
        Doctor doctor = ((DoctorActivity) requireActivity()).getDoctor();

        new AsyncTask<Void, Void, List<Appointment>>() {
            @Override
            protected List<Appointment> doInBackground(Void... voids) {
                if (doctor != null) {
                    return appointmentDao.getAppointmentsForDoctor(doctor.getId());
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

}
