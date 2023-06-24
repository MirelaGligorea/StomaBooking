package com.example.stomabooking.models;

import com.google.firebase.database.PropertyName;

public class ChatRoom {
    @PropertyName("id")
    public String id;
    @PropertyName("user_id")
    public String userId;
    @PropertyName("user_name")
    public String userName;
    @PropertyName("doctor_id")
    public String doctorId;
    @PropertyName("doctor_name")
    public String doctorName;

    public ChatRoom() {
    }

    public ChatRoom(String id, String userId, String userName, String doctorId, String doctorName) {
        this.id = id;
        this.userName = userName;
        this.userId = userId;
        this.doctorId = doctorId;
        this.doctorName = doctorName;
    }
}
