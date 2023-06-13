package com.example.medical_appointments_application.authentification;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.medical_appointments_application.database.AppDatabase;
import com.example.medical_appointments_application.R;
import com.example.medical_appointments_application.database.UserDao;
import com.example.medical_appointments_application.doctor.DoctorActivity;
import com.example.medical_appointments_application.model.User;
import com.example.medical_appointments_application.patient.PatientActivity;

public class SignInFragment extends Fragment {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signInButton;
    private UserDao userDao;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppDatabase userDatabase = AppDatabase.getInstance(requireContext());
        userDao = userDatabase.userDao();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        signInButton = view.findViewById(R.id.signInButton);

        signInButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            signIn(email, password);
        });

        return view;
    }

    private void signIn(String email, String password) {
        new Thread(() -> {
            User user = userDao.getUser(email, password);

            requireActivity().runOnUiThread(() -> {
                if (user != null) {
                    String role = user.getRole();

                    if (role.equals("Doctor")) {
                        Intent intent = new Intent(requireContext(), DoctorActivity.class);
                        startActivity(intent);
                        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                        navController.navigate(R.id.action_signInFragment_to_MainPageDoctorFragment);
                        requireActivity().finishAffinity();
                    } else if (role.equals("Patient")) {
                        Intent intent = new Intent(requireContext(), PatientActivity.class);
                        startActivity(intent);
                        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                        navController.navigate(R.id.action_signInFragment_to_MainPagePatientFragment);
                        requireActivity().finishAffinity();
                    }

                    Toast.makeText(requireContext(), "Sign-in successful!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }



//    private void signIn(String email, String password) {
//        new Thread(() -> {
//            User user = userDao.getUser(email, password);
//
//            requireActivity().runOnUiThread(() -> {
//                if (user != null) {
//                    String role = user.getRole();
//
//                    if (role.equals("Doctor")) {
//                        Intent intent = new Intent(requireContext(), DoctorActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        requireActivity().finish();
//                    } else if (role.equals("Patient")) {
//                        Intent intent = new Intent(requireContext(), PatientActivity.class);
//                        startActivity(intent);
//                    }
//
//                    Toast.makeText(requireContext(), "Sign-in successful!", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }).start();
//    }



}
