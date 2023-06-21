package com.example.medical_appointments_application.database;
import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.medical_appointments_application.model.Appointment;
import com.example.medical_appointments_application.model.Doctor;
import com.example.medical_appointments_application.model.Patient;
import com.example.medical_appointments_application.model.User;

@Database(entities = {User.class, Doctor.class, Patient.class, Appointment.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract DoctorDao doctorDao();

    public abstract PatientDao patientDao();

    public abstract AppointmentDao appointmentDao();
    public abstract UserDao userDao();

//    public static synchronized AppDatabase getInstance(Context context) {
//        if (instance == null) {
//            instance = Room.databaseBuilder(context.getApplicationContext(),
//                            AppDatabase.class, "users-db")
//                    .build();
//        }
//        return instance;
//    }

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "users-db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}

