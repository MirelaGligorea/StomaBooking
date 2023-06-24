package com.example.stomabooking.models;

import com.google.firebase.database.PropertyName;

public class Service {
    @PropertyName("id")
    public String id;
    @PropertyName("title")
    public String title;
    @PropertyName("duration")
    public int duration;
    @PropertyName("price")
    public int price;

    public Service() {
    }

    public Service(String id, String title, int duration, int price) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.price = price;
    }
}
