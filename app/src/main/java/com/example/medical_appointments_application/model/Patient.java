package com.example.medical_appointments_application.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "patients",
        foreignKeys = @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "user_id"),
        indices = {@Index(value = "user_id")}
)
public class Patient implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "patient_id")
    private int id;

    @ColumnInfo(name = "user_id")
    private int userId; // Foreign key column

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "surname")
    private String surname;

    @ColumnInfo(name = "age")
    private int age;

    @ColumnInfo(name = "telephone_number")
    private String telephoneNumber;

    public Patient(int userId, String name, String surname, int age, String telephoneNumber) {
        this.userId = userId;
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.telephoneNumber = telephoneNumber;
    }

    protected Patient(Parcel in) {
        id = in.readInt();
        userId = in.readInt();
        name = in.readString();
        surname = in.readString();
        age = in.readInt();
        telephoneNumber = in.readString();
    }

    public static final Creator<Patient> CREATOR = new Creator<Patient>() {
        @Override
        public Patient createFromParcel(Parcel in) {
            return new Patient(in);
        }

        @Override
        public Patient[] newArray(int size) {
            return new Patient[size];
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(userId);
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeInt(age);
        dest.writeString(telephoneNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Rest of your class code...
}
