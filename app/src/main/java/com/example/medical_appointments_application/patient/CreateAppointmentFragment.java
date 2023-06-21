package com.example.medical_appointments_application.patient;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

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
        // Required empty public constructor
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

        // Fetch doctors in a background thread
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
                // Update your UI with the fetched doctors
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

                // Verify if all fields have valid contents
                if (isValidDate(date) && isValidHour(hour) && selectedDoctor != null) {
                    // Create an appointment object
                    Appointment appointment = new Appointment(selectedDoctor.getId(),patient.getId(), date, hour, "Spital", selectedDoctor.getSpecialization());

                    // Insert the appointment into the database using AsyncTask
                    new AsyncTask<Appointment, Void, Void>() {
                        @SuppressLint("StaticFieldLeak")
                        @Override
                        protected Void doInBackground(Appointment... appointments) {
                            // Perform database operation to insert appointment
                            AppDatabase.getInstance(requireContext()).appointmentDao().insertAppointment(appointments[0]);
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            // Display success message or perform any necessary UI updates
                            Toast.makeText(requireContext(), "Appointment created successfully", Toast.LENGTH_SHORT).show();
                            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                            fragmentManager.popBackStack();
                            Fragment previousFragment = fragmentManager.findFragmentByTag("MainPagePatientFragment");
                            if (previousFragment instanceof MainPagePatientFragment) {
                                ((MainPagePatientFragment) previousFragment).refreshUI();
                            }
                        }
                    }.execute(appointment);
                } else {
                    // Display error message for incomplete fields
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
            // Parse the date string
            Date parsedDate = dateFormat.parse(date);

            // Check if the parsed date matches the input date
            if (!dateFormat.format(parsedDate).equals(date)) {
                return false; // Invalid date format
            }

            // Get the individual components of the date
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parsedDate);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1; // January is represented by 0
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Validate the individual components
            if (year < 1900 || month < 1 || month > 12 || day < 1 || day > 31) {
                return false; // Invalid components
            }

            // Additional checks for specific cases
            // For example, if you want to ensure there are no invalid numbers like the 13th month
            if (month == 2 && day > 29) {
                return false; // Invalid day for February
            }

            // Other validation checks as needed...

            // Date is valid
            return true;
        } catch (ParseException e) {
            return false; // Invalid date format
        }
    }

    private boolean isValidHour(String hour) {
        if(hour.isEmpty())
            return false;
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        timeFormat.setLenient(false);

        try {
            // Parse the hour string
            Date parsedTime = timeFormat.parse(hour);

            // Check if the parsed time matches the input hour
            if (!timeFormat.format(parsedTime).equals(hour)) {
                return false; // Invalid hour format
            }

            // Get the individual components of the time
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parsedTime);
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            // Validate the individual components
            if (hourOfDay < 0 || hourOfDay > 23 || minute < 0 || minute > 59) {
                return false; // Invalid components
            }

            // Hour is valid
            return true;
        } catch (ParseException e) {
            return false; // Invalid hour format
        }
    }



//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_create_appointment, container, false);
//        dateEditText = view.findViewById(R.id.dateEditText);
//        hourEditText = view.findViewById(R.id.hourEditText);
//        doctorSpinner = view.findViewById(R.id.doctorSpinner);
//        submitButton = view.findViewById(R.id.submitButton);
//
//        // Fetch doctors in a background thread
//        new AsyncTask<Void, Void, List<Doctor>>() {
//            @SuppressLint("StaticFieldLeak")
//            @Override
//            protected List<Doctor> doInBackground(Void... voids) {
//                DoctorDao doctorDao = AppDatabase.getInstance(requireContext()).doctorDao();
//                return doctorDao.getAllDoctors();
//            }
//
//            @Override
//            protected void onPostExecute(List<Doctor> doctors) {
//                super.onPostExecute(doctors);
//                // Update your UI with the fetched doctors
//                doctorList = doctors;
//                ArrayAdapter<Doctor> doctorAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, doctorList);
//                doctorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                doctorSpinner.setAdapter(doctorAdapter);
//            }
//        }.execute();
//
//        // Inflate the layout for this fragment
//
//        return view;
//    }

}