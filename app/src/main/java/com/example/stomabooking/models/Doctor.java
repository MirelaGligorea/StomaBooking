package com.example.stomabooking.models;

import com.google.firebase.database.PropertyName;

public class Doctor {
    @PropertyName("id")
    public String id;
    @PropertyName("name")
    public String name;
    @PropertyName("email")
    public String email;
    @PropertyName("password")
    public String password;

    public Doctor() {
    }

    public Doctor(String id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
