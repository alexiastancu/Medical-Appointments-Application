package com.example.medical_appointments_application.patient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.medical_appointments_application.R;
import com.example.medical_appointments_application.database.AppDatabase;
import com.example.medical_appointments_application.database.DoctorDao;
import com.example.medical_appointments_application.model.Appointment;
import com.example.medical_appointments_application.model.Doctor;
import com.example.medical_appointments_application.model.Patient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateAppointmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateAppointmentFragment extends Fragment {


    private EditText dateEditText;
    private EditText hourEditText;
    private Spinner doctorSpinner;
    private Button submitButton;

    private List<Doctor> doctorList;

    public CreateAppointmentFragment() {

    }

    public static CreateAppointmentFragment newInstance(String param1, String param2) {
        CreateAppointmentFragment fragment = new CreateAppointmentFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_appointment, container, false);
        dateEditText = view.findViewById(R.id.dateEditText);
        hourEditText = view.findViewById(R.id.hourEditText);
        doctorSpinner = view.findViewById(R.id.doctorSpinner);
        submitButton = view.findViewById(R.id.submitButton);
        Patient patient = ((PatientActivity) requireActivity()).getPatient();

        new AsyncTask<Void, Void, List<Doctor>>() {
            @SuppressLint("StaticFieldLeak")
            @Override
            protected List<Doctor> doInBackground(Void... voids) {
                DoctorDao doctorDao = AppDatabase.getInstance(requireContext()).doctorDao();
                return doctorDao.getAllDoctors();
            }

            @Override
            protected void onPostExecute(List<Doctor> doctors) {
                super.onPostExecute(doctors);
                doctorList = doctors;
                ArrayAdapter<Doctor> doctorAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, doctorList);
                doctorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                doctorSpinner.setAdapter(doctorAdapter);
            }
        }.execute();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = dateEditText.getText().toString().trim();
                String hour = hourEditText.getText().toString().trim();
                Doctor selectedDoctor = (Doctor) doctorSpinner.getSelectedItem();


                if (isValidDate(date) && isValidHour(hour) && selectedDoctor != null) {
                    Appointment appointment = new Appointment(selectedDoctor.getId(),patient.getId(), date, hour, "Spital", selectedDoctor.getSpecialization());

                    new AsyncTask<Appointment, Void, Void>() {
                        @SuppressLint("StaticFieldLeak")
                        @Override
                        protected Void doInBackground(Appointment... appointments) {
                            AppDatabase.getInstance(requireContext()).appointmentDao().insertAppointment(appointments[0]);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            requireActivity().setResult(Activity.RESULT_OK);
                            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_patient);
                            navController.popBackStack();
                        }
                    }.execute(appointment);
                } else {

                    Toast.makeText(requireContext(), "Please correct the inserted fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private boolean isValidDate(String date) {
        if(date.isEmpty())
            return false;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat.setLenient(false);

        try {

            Date parsedDate = dateFormat.parse(date);

            if (!dateFormat.format(parsedDate).equals(date)) {
                return false;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parsedDate);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1; // January is represented by 0
            int day = calendar.get(Calendar.DAY_OF_MONTH);


            if (year < 1900 || month < 1 || month > 12 || day < 1 || day > 31) {
                return false;
            }

            if (month == 2 && day > 29) {
                return false; // Invalid day for February
            }

            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private boolean isValidHour(String hour) {
        if(hour.isEmpty())
            return false;
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        timeFormat.setLenient(false);

        try {

            Date parsedTime = timeFormat.parse(hour);

            if (!timeFormat.format(parsedTime).equals(hour)) {
                return false;
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parsedTime);
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            if (hourOfDay < 0 || hourOfDay > 23 || minute < 0 || minute > 59) {
                return false;
            }


            return true;
        } catch (ParseException e) {
            return false;
        }
    }



}