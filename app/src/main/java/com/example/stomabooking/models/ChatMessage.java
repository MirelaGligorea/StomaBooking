package com.example.stomabooking.models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ChatMessage {
    @PropertyName("id")
    public String id;
    @PropertyName("room_id")
    public String roomId;
    @PropertyName("user_name")
    public String userName;
    @PropertyName("doctor_name")
    public String doctorName;
    @PropertyName("message")
    public String message;
    @PropertyName("is_from_doctor")
    public boolean isFromDoctor;
    @PropertyName("timestamp")
    public long timestamp;

    public ChatMessage() {
    }

    public ChatMessage(String id, String roomId, String userName, String doctorName, String message, boolean isFromDoctor, long timestamp) {
        this.id = id;
        this.roomId = roomId;
        this.userName = userName;
        this.doctorName = doctorName;
        this.message = message;
        this.isFromDoctor = isFromDoctor;
        this.timestamp = timestamp;
    }

    @Exclude
    public String getDateAndTime() {
        LocalDateTime localDateTime = Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()).toLocalDateTime();
        return localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm"));
    }
}
