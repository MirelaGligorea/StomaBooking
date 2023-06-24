package com.example.stomabooking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.stomabooking.R;
import com.example.stomabooking.managers.DatabaseManager;
import com.example.stomabooking.managers.UserManager;
import com.example.stomabooking.models.Doctor;
import com.example.stomabooking.models.User;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEt;
    private EditText passwordEt;
    private Button loginBtn;
    private Button registerBtn;
    private FrameLayout loadingFl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        DatabaseManager.getInstance().init(() -> loadingFl.setVisibility(View.GONE));
        initViews();
        setClickListeners();
        emailEt.setText("irina.rimes@gmail.com");
        passwordEt.setText("123456789");
    }

    private void initViews() {
        emailEt = findViewById(R.id.etEmail);
        passwordEt = findViewById(R.id.etPassword);
        loginBtn = findViewById(R.id.btnLogin);
        registerBtn = findViewById(R.id.btnRegister);
        loadingFl = findViewById(R.id.flLoading);
    }

    private void setClickListeners() {
        loginBtn.setOnClickListener(v -> {
            String email = emailEt.getText().toString();
            String password = passwordEt.getText().toString();
            if (!isDataValid(email, password)) {
                return;
            }

            User user = DatabaseManager.getInstance().getUserByEmailAndPassword(email, password);
            if (user != null) {
                UserManager.getInstance().setCurrentUser(user);
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Doctor doctor = DatabaseManager.getInstance().getDoctorByEmailAndPassword(email, password);
                if (doctor != null) {
                    UserManager.getInstance().setCurrentDoctor(doctor);
                    Intent intent = new Intent(this, MainDoctorActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.error_incorrect_email_or_password, Toast.LENGTH_LONG).show();
                }
            }
        });
        registerBtn.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
    }

    private boolean isDataValid(String email, String password) {
        if (email.isEmpty()) {
            Toast.makeText(this, R.string.error_email_must_be_filled, Toast.LENGTH_LONG).show();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, R.string.error_email_wrong_format, Toast.LENGTH_LONG).show();
            return false;
        }
        if (password.isEmpty()) {
            Toast.makeText(this, R.string.error_password_must_be_filled, Toast.LENGTH_LONG).show();
            return false;
        }
        if (password.length() < 8) {
            Toast.makeText(this, R.string.error_password_length, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}