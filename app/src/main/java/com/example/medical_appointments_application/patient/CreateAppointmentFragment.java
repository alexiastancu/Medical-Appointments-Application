package com.example.medical_appointments_application.patient;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.medical_appointments_application.R;
import com.example.medical_appointments_application.database.AppDatabase;
import com.example.medical_appointments_application.database.DoctorDao;
import com.example.medical_appointments_application.model.Doctor;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateAppointmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateAppointmentFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private EditText dateEditText;
    private EditText hourEditText;
    private Spinner doctorSpinner;
    private Button submitButton;

    private List<Doctor> doctorList;

    public CreateAppointmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateAppointmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateAppointmentFragment newInstance(String param1, String param2) {
        CreateAppointmentFragment fragment = new CreateAppointmentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DoctorDao doctorDao = AppDatabase.getInstance(requireContext()).doctorDao();

        View view = inflater.inflate(R.layout.fragment_create_appointment, container, false);
        dateEditText = view.findViewById(R.id.dateEditText);
        hourEditText = view.findViewById(R.id.hourEditText);
        doctorSpinner = view.findViewById(R.id.doctorSpinner);
        submitButton = view.findViewById(R.id.submitButton);
        doctorList= doctorDao.getAllDoctors();
        ArrayAdapter<Doctor> doctorAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, doctorList);
        doctorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Setare adapter pentru spinner
        doctorSpinner.setAdapter(doctorAdapter);
        // Inflate the layout for this fragment
        return view;

    }
}