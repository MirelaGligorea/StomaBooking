package com.example.stomabooking.models;

import com.google.firebase.database.PropertyName;

public class AppointmentImage {
    @PropertyName("id")
    public String id;
    @PropertyName("appointment_id")
    public String appointmentId;
    @PropertyName("url")
    public String url;

    public AppointmentImage() {
    }

    public AppointmentImage(String id, String appointmentId, String url) {
        this.id = id;
        this.appointmentId = appointmentId;
        this.url = url;
    }
}
