package com.example.medical_appointments_application.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "doctors",
        foreignKeys = @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "user_id"),
        indices = {@Index(value = "user_id")}
)
public class Doctor implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "doctor_id")
    private int id;

    @ColumnInfo(name = "user_id")
    private int userId; // Foreign key column

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "surname")
    private String surname;

    @ColumnInfo(name = "specialization")
    private String specialization;

    @ColumnInfo(name = "email")
    private String email;

    public Doctor(int userId, String name, String surname, String specialization, String email) {
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.specialization = specialization;
        this.email = email;
    }

    protected Doctor(Parcel in) {
        id = in.readInt();
        userId = in.readInt();
        name = in.readString();
        surname = in.readString();
        specialization = in.readString();
        email = in.readString();
    }

    public static final Creator<Doctor> CREATOR = new Creator<Doctor>() {
        @Override
        public Doctor createFromParcel(Parcel in) {
            return new Doctor(in);
        }

        @Override
        public Doctor[] newArray(int size) {
            return new Doctor[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(userId);
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(specialization);
        dest.writeString(email);
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
