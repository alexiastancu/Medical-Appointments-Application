package com.example.medical_appointments_application.doctor;

import android.app.AlertDialog;
import android.os.Bundle;

import com.example.medical_appointments_application.databinding.ActivityDoctorBinding;
import com.example.medical_appointments_application.model.Doctor;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import com.example.medical_appointments_application.R;

public class DoctorActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityDoctorBinding binding;

    private Doctor doctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDoctorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Retrieve the doctor object from the intent
        doctor = getIntent().getParcelableExtra("doctor");


        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_doctor);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        navController.setGraph(R.navigation.nav_graph_doctor);

//        binding.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }



    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_doctor);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_doctor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_personal_info) {
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_doctor);
            navController.navigate(R.id.action_mainPageDoctorFragment_to_doctorDetailsFragment);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private int backPressCounter = 0;

    @Override
    public void onBackPressed() {
        if (backPressCounter < 2) {
            backPressCounter++;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Exit Confirmation")
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Exit the app
                        finish();
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        // Reset the back press counter
                        backPressCounter = 0;
                        dialog.dismiss();
                    })
                    .show();
        }
    }


    public Doctor getDoctor() {
        return this.doctor;
    }
}