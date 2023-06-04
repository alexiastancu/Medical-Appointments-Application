package com.example.medical_appointments_application;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.medical_appointments_application.R;

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
        // Perform sign-in operation and validate user credentials on a background thread
        new Thread(() -> {
            User user = userDao.getUser(email, password);

            requireActivity().runOnUiThread(() -> {
                if (user != null) {
                    // Sign-in successful, navigate to the next screen
                    Toast.makeText(requireContext(), "Sign-in successful!", Toast.LENGTH_SHORT).show();
                } else {
                    // Invalid credentials, display an error message
                    Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}
