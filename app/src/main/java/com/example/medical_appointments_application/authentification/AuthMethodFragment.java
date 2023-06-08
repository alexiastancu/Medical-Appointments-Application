package com.example.medical_appointments_application.authentification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.medical_appointments_application.R;


public class AuthMethodFragment extends Fragment {
    public AuthMethodFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_auth_method, container, false);

        Button signInButton = view.findViewById(R.id.signInButton);
        Button signUpButton = view.findViewById(R.id.signUpButton);

        signInButton.setOnClickListener(v -> {
            // Navigate to the Sign In fragment
            NavHostFragment.findNavController(AuthMethodFragment.this)
                    .navigate(R.id.action_authMethodFragment_to_signInFragment);
        });

        signUpButton.setOnClickListener(v -> {
            // Navigate to the Sign Up fragment
            NavHostFragment.findNavController(AuthMethodFragment.this)
                    .navigate(R.id.action_authMethodFragment_to_signUpFragment);
        });

        return view;
    }

}