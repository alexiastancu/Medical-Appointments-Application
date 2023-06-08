package com.example.medical_appointments_application.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.medical_appointments_application.model.Appointment;

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

    // Add other query methods as needed
}
