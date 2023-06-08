package com.example.medical_appointments_application.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.medical_appointments_application.model.Doctor;

import java.util.List;

@Dao
public interface DoctorDao {

    @Insert
    void insert(Doctor doctor);

    @Update
    void update(Doctor doctor);

    @Delete
    void delete(Doctor doctor);

    @Query("SELECT * FROM doctors")
    List<Doctor> getAllDoctors();

    // Add other query methods as needed
}
