package com.example.medical_appointments_application.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.medical_appointments_application.model.Appointment;
import com.example.medical_appointments_application.model.Doctor;
import com.example.medical_appointments_application.model.Patient;

import java.util.List;

@Dao
public interface AppointmentDao {

    @Insert
    void insertAppointment(Appointment appointment);

    @Update
    void updateAppointment(Appointment appointment);

    @Delete
    void deleteAppointment(Appointment appointment);

    @Query("SELECT * FROM appointments")
    List<Appointment> getAllAppointments();

    @Query("SELECT * FROM appointments WHERE patient_id = :patientId")
    List<Appointment> getAppointmentsForPatient(int patientId);

    @Query("SELECT * FROM appointments WHERE doctor_id = :doctorId")
    List<Appointment> getAppointmentsForDoctor(int doctorId);




}
