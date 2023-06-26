package com.example.medical_appointments_application.authentification;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.medical_appointments_application.database.AppDatabase;
import com.example.medical_appointments_application.R;
import com.example.medical_appointments_application.database.DoctorDao;
import com.example.medical_appointments_application.database.PatientDao;
import com.example.medical_appointments_application.database.UserDao;
import com.example.medical_appointments_application.doctor.DoctorActivity;
import com.example.medical_appointments_application.model.Doctor;
import com.example.medical_appointments_application.model.Patient;
import com.example.medical_appointments_application.model.User;
import com.example.medical_appointments_application.patient.PatientActivity;

public class SignInFragment extends Fragment {
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signInButton;
    private UserDao userDao;
    private DoctorDao doctorDao;
    private PatientDao patientDao;
    private SharedPreferences sharedPreferences;

    public SignInFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppDatabase userDatabase = AppDatabase.getInstance(requireContext());
        userDao = userDatabase.userDao();
        doctorDao = userDatabase.doctorDao();
        patientDao = userDatabase.patientDao();

        sharedPreferences = requireContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        signInButton = view.findViewById(R.id.signInButton);

        // Retrieve the saved values from SharedPreferences
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString("email", "");
        String savedPassword = sharedPreferences.getString("password", "");

        // Set the saved values to the EditText fields
        emailEditText.setText(savedEmail);
        passwordEditText.setText(savedPassword);

        signInButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            signIn(email, password);
        });

        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private void signIn(String email, String password) {
        new AsyncTask<Void, Void, User>() {
            @Override
            protected User doInBackground(Void... voids) {
                return userDao.getUser(email, password);
            }

            @SuppressLint("StaticFieldLeak")
            @Override
            protected void onPostExecute(User user) {
                if (user != null) {
                    String role = user.getRole();

                    if (role.equals("Doctor")) {
                        new AsyncTask<Void, Void, Doctor>() {
                            @Override
                            protected Doctor doInBackground(Void... voids) {
                                return doctorDao.getDoctorByUserId(user.getId());
                            }

                            @Override
                            protected void onPostExecute(Doctor doctor) {
                                if (doctor != null) {
                                    sharedPreferences.edit().putInt("userId", user.getId()).apply();
                                    Intent intent = new Intent(requireContext(), DoctorActivity.class);
                                    intent.putExtra("doctor", doctor);
                                    startActivity(intent);
                                    requireActivity().finishAffinity();
                                } else {
                                    Toast.makeText(requireContext(), "Invalid doctor account", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }.execute();
                    } else if (role.equals("Patient")) {
                        new AsyncTask<Void, Void, Patient>() {
                            @Override
                            protected Patient doInBackground(Void... voids) {
                                return patientDao.getPatientByUserId(user.getId());
                            }

                            @Override
                            protected void onPostExecute(Patient patient) {
                                if (patient != null) {
                                    sharedPreferences.edit().putInt("userId", user.getId()).apply();
                                    Intent intent = new Intent(requireContext(), PatientActivity.class);
                                    intent.putExtra("patient", patient);
                                    startActivity(intent);
                                    requireActivity().finishAffinity();
                                } else {
                                    Toast.makeText(requireContext(), "Invalid patient account", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }.execute();
                    }

                    Toast.makeText(requireContext(), "Sign-in successful!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}
