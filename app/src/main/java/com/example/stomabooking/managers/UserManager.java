package com.example.stomabooking.managers;

import com.example.stomabooking.models.Doctor;
import com.example.stomabooking.models.User;

public class UserManager {

    private static final UserManager mInstance = new UserManager();

    private User currentUser;
    private Doctor currentDoctor;

    private UserManager() {
    }

    public static UserManager getInstance() {
        return mInstance;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public Doctor getCurrentDoctor() {
        return currentDoctor;
    }

    public void setCurrentDoctor(Doctor currentDoctor) {
        this.currentDoctor = currentDoctor;
    }
}
