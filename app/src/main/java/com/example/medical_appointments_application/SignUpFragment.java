package com.example.medical_appointments_application;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.medical_appointments_application.R;

public class SignUpFragment extends Fragment {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Spinner roleSpinner;
    private Button signUpButton;
    private UserDao userDao;

    public SignUpFragment() {
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
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        emailEditText = view.findViewById(R.id.emailEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        roleSpinner = view.findViewById(R.id.roleSpinner);
        signUpButton = view.findViewById(R.id.signUpButton);

        // Set up the spinner with role options
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.roles_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);

        signUpButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String role = roleSpinner.getSelectedItem().toString();
            signUp(email, password, role);
        });

        return view;
    }

    private void signUp(String email, String password, String role) {
        // Perform sign-up operation on a background thread
        new Thread(() -> {
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

            requireActivity().runOnUiThread(() -> {
                // Sign-up successful
                Toast.makeText(requireContext(), "Sign-up successful!", Toast.LENGTH_SHORT).show();
                // Clear the input fields
                emailEditText.setText("");
                passwordEditText.setText("");
                roleSpinner.setSelection(0);
            });
        }).start();
    }
}
