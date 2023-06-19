package com.example.medical_appointments_application.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.medical_appointments_application.model.Patient;

import java.util.List;

@Dao
public interface PatientDao {

    @Insert
    void insert(Patient patient);

    @Update
    void update(Patient patient);

    @Delete
    void delete(Patient patient);

    @Query("SELECT * FROM patients")
    List<Patient> getAllPatients();

    @Query("SELECT * FROM patients WHERE patient_id = :patientId")
    Patient getPatientById(int patientId);

    @Query("SELECT * FROM patients WHERE user_id = :userId")
    Patient getPatientByUserId(int userId);
}
