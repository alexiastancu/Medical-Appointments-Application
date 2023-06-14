package com.example.medical_appointments_application.model;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medical_appointments_application.R;
import com.example.medical_appointments_application.database.AppointmentDao;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {
    private List<Appointment> appointments;
    private static AppointmentDao appointmentDao;

    public AppointmentAdapter(List<Appointment> appointments, AppointmentDao appointmentDao) {
        this.appointments = appointments;
        this.appointmentDao = appointmentDao;
    }

    public AppointmentAdapter(AppointmentDao appointmentDao) {
        this.appointments = new ArrayList<>();
        this.appointmentDao = appointmentDao;
    }

    public void setAppointments(List<Appointment> newAppointments) {
        appointments.clear();
        appointments.addAll(newAppointments);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Appointment appointment = appointments.get(position);
        holder.bind(appointment);
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }


public class ViewHolder extends RecyclerView.ViewHolder {
    private TextView patientNameTextView;
    private TextView doctorNameTextView;
    private TextView locationTextView;
    private TextView dateTimeTextView;

    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        patientNameTextView = itemView.findViewById(R.id.patient_name);
        doctorNameTextView = itemView.findViewById(R.id.doctor_name);
        locationTextView = itemView.findViewById(R.id.location);
        dateTimeTextView = itemView.findViewById(R.id.date_time);
    }

    public void bind(Appointment appointment) {
        new DatabaseTask(this, appointmentDao).execute(appointment);
        locationTextView.setText(appointment.getLocation());
        dateTimeTextView.setText(appointment.getDate() + " " + appointment.getTime());
    }
}

    private static class DatabaseTask extends AsyncTask<Appointment, Void, String[]> {
        private WeakReference<ViewHolder> viewHolderReference;
        private AppointmentDao appointmentDao;

        public DatabaseTask(ViewHolder viewHolder, AppointmentDao appointmentDao) {
            this.viewHolderReference = new WeakReference<>(viewHolder);
            this.appointmentDao = appointmentDao;
        }

        @Override
        protected String[] doInBackground(Appointment... appointments) {
            Appointment appointment = appointments[0];
            Doctor doctor = appointmentDao.getDoctorById(appointment.getDoctorId());
            Patient patient = appointmentDao.getPatientById(appointment.getPatientId());

            String doctorName = "Doctor: " + doctor.getName() + " " + doctor.getSurname();
            String patientName = "Patient: " + patient.getName() + " " + patient.getSurname();

            return new String[]{doctorName, patientName};
        }

        @Override
        protected void onPostExecute(String[] results) {
            ViewHolder viewHolder = viewHolderReference.get();
            if (viewHolder != null) {
                viewHolder.patientNameTextView.setText(results[1]);
                viewHolder.doctorNameTextView.setText(results[0]);
            }
        }
    }


}

