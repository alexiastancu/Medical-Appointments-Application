package com.example.medical_appointments_application.authentification;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.medical_appointments_application.database.AppDatabase;
import com.example.medical_appointments_application.R;
import com.example.medical_appointments_application.database.AppointmentDao;
import com.example.medical_appointments_application.database.DoctorDao;
import com.example.medical_appointments_application.database.PatientDao;
import com.example.medical_appointments_application.database.UserDao;
import com.example.medical_appointments_application.model.Doctor;
import com.example.medical_appointments_application.model.Patient;
import com.example.medical_appointments_application.model.User;

public class SignUpFragment extends Fragment {

    private EditText nameEditText;
    private EditText surnameEditText;
    private EditText ageEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Spinner roleSpinner;
    private Spinner specializationSpinner;
    private Button signUpButton;
    private UserDao userDao;
    private DoctorDao doctorDao;
    private PatientDao patientDao;
    private AppointmentDao appointmentDao;

    public SignUpFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppDatabase database = AppDatabase.getInstance(requireContext());
        userDao = database.userDao();
        doctorDao = database.doctorDao();
        patientDao = database.patientDao();
        appointmentDao = database.appointmentDao();
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
//
//        emailEditText = view.findViewById(R.id.emailEditText);
//        passwordEditText = view.findViewById(R.id.passwordEditText);
//        roleSpinner = view.findViewById(R.id.roleSpinner);
//        specializationSpinner = view.findViewById(R.id.specializationSpinner);
//        signUpButton = view.findViewById(R.id.signUpButton);
//
//        // Set up the spinner with role options
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
//                R.array.roles_array, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        roleSpinner.setAdapter(adapter);
//
//        signUpButton.setOnClickListener(v -> {
//            String email = emailEditText.getText().toString();
//            String password = passwordEditText.getText().toString();
//            String role = roleSpinner.getSelectedItem().toString();
//            signUp(email, password, role);
//        });
//
//        return view;
//    }
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
    nameEditText = view.findViewById(R.id.nameEditText);
    surnameEditText = view.findViewById(R.id.surnameEditText);
    ageEditText = view.findViewById(R.id.ageEditText);
    phoneEditText = view.findViewById(R.id.telephoneEditText);
    emailEditText = view.findViewById(R.id.emailEditText);
    passwordEditText = view.findViewById(R.id.passwordEditText);
    roleSpinner = view.findViewById(R.id.roleSpinner);
    specializationSpinner = view.findViewById(R.id.specializationSpinner);
    signUpButton = view.findViewById(R.id.signUpButton);

    // Set up the spinner with role options
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
            R.array.roles_array, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    roleSpinner.setAdapter(adapter);

    // Set up the listener for the role spinner
    roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selectedRole = parent.getItemAtPosition(position).toString();

            if (selectedRole.equals("Doctor")) {
                specializationSpinner.setVisibility(View.VISIBLE);
                ageEditText.setVisibility(View.GONE);
            } else {
                specializationSpinner.setVisibility(View.GONE);
                ageEditText.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // Handle case when nothing is selected
        }
    });

    signUpButton.setOnClickListener(v -> {
        String name = nameEditText.getText().toString();
        String surname = surnameEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String role = roleSpinner.getSelectedItem().toString();
        if(role.equals("Doctor")) {
            String specialization = specializationSpinner.getSelectedItem().toString();
            signUp(name, surname, email, password, role, specialization);
        }
        else
        {
            signUp(name, surname, email, password, role, "");
        }
    });

    return view;
}

    private void signUp(String name, String surname, String email, String password, String role, String specialization) {
        // Perform sign-up operation on a background thread
        new Thread(() -> {
            // Check for empty fields
            if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty()) {
                requireActivity().runOnUiThread(() -> {
                    // Display an error message for empty fields
                    Toast.makeText(requireContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                });
                return;
            }

            // Check if the user already exists
            User existingUser = userDao.getUserByEmail(email);
            if (existingUser != null) {
                requireActivity().runOnUiThread(() -> {
                    // User with the same email already exists
                    Toast.makeText(requireContext(), "User with this email already exists", Toast.LENGTH_SHORT).show();
                });
                return;
            }

            // Create a new user and insert into the database
            User newUser = new User(email, password, role);
            userDao.insert(newUser);
            int userId = newUser.getId();

            // Check the user's role and insert into the respective table
            if (role.equals("Doctor")) {
                // Check for empty specialization field
                if (specialization.isEmpty()) {
                    requireActivity().runOnUiThread(() -> {
                        // Display an error message for empty specialization
                        Toast.makeText(requireContext(), "Please enter specialization", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }

                Doctor newDoctor = new Doctor(name, surname, specialization, email);
                doctorDao.insert(newDoctor);
            } else if (role.equals("Patient")) {
                // Check for empty age field
                if (ageEditText.getText().toString().isEmpty()) {
                    requireActivity().runOnUiThread(() -> {
                        // Display an error message for empty age
                        Toast.makeText(requireContext(), "Please enter age", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }

                Patient newPatient = new Patient(name, surname, Integer.parseInt(ageEditText.getText().toString()), phoneEditText.getText().toString());
                patientDao.insert(newPatient);
            }

            requireActivity().runOnUiThread(() -> {
                // Sign-up successful
                Toast.makeText(requireContext(), "Sign-up successful!", Toast.LENGTH_SHORT).show();
                nameEditText.setText("");
                surnameEditText.setText("");
                ageEditText.setText("");
                phoneEditText.setText("");
                emailEditText.setText("");
                passwordEditText.setText("");
                roleSpinner.setSelection(0);
                specializationSpinner.setSelection(0);
            });
        }).start();
    }



//private void signUp(String name, String surname, String email, String password, String role, String specialization) {
//    // Perform sign-up operation on a background thread
//    new Thread(() -> {
//        // Check if the user already exists
//        User existingUser = userDao.getUserByEmail(email);
//        if (existingUser != null) {
//            requireActivity().runOnUiThread(() -> {
//                // User with the same email already exists
//                Toast.makeText(requireContext(), "User with this email already exists", Toast.LENGTH_SHORT).show();
//            });
//            return;
//        }
//
//        // Create a new user and insert into the database
//        User newUser = new User(email, password, role);
//        userDao.insert(newUser);
//        int userId = newUser.getId();
//
//        // Check the user's role and insert into the respective table
//        if (role.equals("Doctor")) {
//            Doctor newDoctor = new Doctor(name, surname, specialization, email);
//            doctorDao.insert(newDoctor);
//        } else if (role.equals("Patient")) {
//            Patient newPatient = new Patient(name, surname, Integer.parseInt(ageEditText.getText().toString()), phoneEditText.getText().toString());
//            patientDao.insert(newPatient);
//        }
//
//        requireActivity().runOnUiThread(() -> {
//            // Sign-up successful
//            Toast.makeText(requireContext(), "Sign-up successful!", Toast.LENGTH_SHORT).show();
//            nameEditText.setText("");
//            surnameEditText.setText("");
//            ageEditText.setText("");
//            phoneEditText.setText("");
//            emailEditText.setText("");
//            passwordEditText.setText("");
//            roleSpinner.setSelection(0);
//            specializationSpinner.setSelection(0);
//        });
//    }).start();
//}

}
