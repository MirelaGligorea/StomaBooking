package com.example.stomabooking.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.stomabooking.R;
import com.example.stomabooking.managers.DatabaseManager;
import com.example.stomabooking.managers.UserManager;
import com.example.stomabooking.models.User;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

public class RegisterActivity extends AppCompatActivity {
    private ImageButton backIb;
    private EditText lastNameEt;
    private EditText firstNameEt;
    private EditText birthdateEt;
    private View birthdateOverlay;
    private EditText emailEt;
    private EditText passwordEt;
    private Button registerBtn;

    private LocalDate birthdate = LocalDate.now();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        setClickListeners();
    }

    private void initViews() {
        backIb = findViewById(R.id.ibBack);
        lastNameEt = findViewById(R.id.etLastName);
        firstNameEt = findViewById(R.id.etFirstName);
        birthdateEt = findViewById(R.id.etBirthdate);
        birthdateOverlay = findViewById(R.id.vBirthdate);
        emailEt = findViewById(R.id.etEmail);
        passwordEt = findViewById(R.id.etPassword);
        registerBtn = findViewById(R.id.btnRegister);
    }

    private void setClickListeners() {
        backIb.setOnClickListener(v -> onBackPressed());

        birthdateOverlay.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                R.style.DialogTheme,
                (view, year, month, dayOfMonth) -> {
                    birthdate = LocalDate.of(year, month + 1, dayOfMonth);
                    birthdateEt.setText(birthdate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                },
                birthdate.getYear(),
                birthdate.getMonthValue() - 1,
                birthdate.getDayOfMonth()
            );
            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            datePickerDialog.show();
            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.pink));
            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.pink));
        });

        registerBtn.setOnClickListener(v -> {
            String firstName = firstNameEt.getText().toString();
            String lastName = lastNameEt.getText().toString();
            String birthdate = birthdateEt.getText().toString();
            String email = emailEt.getText().toString();
            String password = passwordEt.getText().toString();
            if (!isDataValid(firstName, lastName, birthdate, email, password)) {
                return;
            }

            User user = new User(UUID.randomUUID().toString(), firstName, lastName, birthdate, email, password);
            DatabaseManager.getInstance().addUser(user);
            UserManager.getInstance().setCurrentUser(user);

            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    private boolean isDataValid(String firstName, String lastName, String birthdate, String email, String password) {
        if (lastName.isEmpty()) {
            Toast.makeText(this, R.string.error_last_name_must_be_filled, Toast.LENGTH_LONG).show();
            return false;
        }
        if (firstName.isEmpty()) {
            Toast.makeText(this, R.string.error_first_name_must_be_filled, Toast.LENGTH_LONG).show();
            return false;
        }
        if (birthdate.isEmpty()) {
            Toast.makeText(this, R.string.error_birthdate_must_be_filled, Toast.LENGTH_LONG).show();
            return false;
        }
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