package com.example.stomabooking.models;

import com.google.firebase.database.PropertyName;

public class User {
    @PropertyName("id")
    public String id;
    @PropertyName("parent_id")
    public String parentId;
    @PropertyName("first_name")
    public String firstName;
    @PropertyName("last_name")
    public String lastName;
    @PropertyName("birthdate")
    public String birthdate;
    @PropertyName("email")
    public String email;
    @PropertyName("password")
    public String password;
    @PropertyName("is_associated_user")
    public boolean isAssociatedUser;

    public User() {
    }

    public User(String id, String firstName, String lastName, String birthdate, String email, String password) {
        this.id = id;
        this.parentId = "";
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.email = email;
        this.password = password;
        this.isAssociatedUser = false;
    }

    public User(String id, String parentId, String firstName, String lastName, String birthdate) {
        this.id = id;
        this.parentId = parentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.email = "";
        this.password = "";
        this.isAssociatedUser = true;
    }
}
