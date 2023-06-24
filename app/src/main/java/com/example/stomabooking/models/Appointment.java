package com.example.stomabooking.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.util.ArrayList;

public class Appointment {
    @PropertyName("id")
    public String id;
    @PropertyName("user_id")
    public String userId;
    @PropertyName("patient_name")
    public String patientName;
    @PropertyName("doctor_id")
    public String doctorId;
    @PropertyName("doctor_name")
    public String doctorName;
    @PropertyName("date")
    public String date;
    @PropertyName("start_hour")
    public String startHour;
    @PropertyName("end_hour")
    public String endHour;
    @PropertyName("title")
    public String title;
    @PropertyName("duration")
    public int duration;
    @PropertyName("price")
    public int price;
    @Exclude
    public ArrayList<AppointmentImage> images = new ArrayList<>();

    public Appointment() {
    }

    public Appointment(String id, String userId, String patientName, String doctorId, String doctorName, String date, String startHour, String endHour, String title, int duration, int price) {
        this.id = id;
        this.userId = userId;
        this.patientName = patientName;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.date = date;
        this.startHour = startHour;
        this.endHour = endHour;
        this.title = title;
        this.duration = duration;
        this.price = price;
    }
}
