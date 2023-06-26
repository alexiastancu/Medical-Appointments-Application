package com.example.medical_appointments_application.patient;

import android.app.AlertDialog;
import android.os.Bundle;

import com.example.medical_appointments_application.databinding.ActivityPatientBinding;
import com.example.medical_appointments_application.model.Patient;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.example.medical_appointments_application.R;

public class PatientActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityPatientBinding binding;
    private Patient patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPatientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        patient = getIntent().getParcelableExtra("patient");

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_patient);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        navController.setGraph(R.navigation.nav_graph_patient);

        binding.fab.setOnClickListener(view -> {
            navController.navigate(R.id.action_mainPagePatientFragment_to_CreateAppointmentFragment);
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_patient);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_patient, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_personal_info) {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_patient);
            navController.navigate(R.id.action_mainPagePatientFragment_to_PatientDetailsFragment);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit Confirmation")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    finish();
                })
                .setNegativeButton("No", (dialog, which) -> {
                    // Dismiss the dialog and do nothing
                    dialog.dismiss();
                })
                .show();
    }

    public Patient getPatient() {
        return patient;
    }

}