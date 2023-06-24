package com.example.stomabooking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stomabooking.R;
import com.example.stomabooking.adapters.AppointmentAdapter;
import com.example.stomabooking.managers.DatabaseManager;
import com.example.stomabooking.managers.UserManager;
import com.example.stomabooking.models.Appointment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class MainDoctorActivity extends AppCompatActivity {
    private ImageView calendarArrowLeftIv;
    private TextView calendarDateTv;
    private ImageView calendarArrowRightIv;
    private TextView emptyAppointmentsTv;
    private FloatingActionButton chatFab;

    private LocalDate selectedDate = LocalDate.now();
    private final ArrayList<Appointment> appointmentsList = new ArrayList<>();
    private AppointmentAdapter appointmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_doctor);

        initViews();
        setClickListeners();
        updateCalendar();
        calendarArrowLeftIv.setColorFilter(ContextCompat.getColor(this, R.color.black_transparent));
    }

    private void initViews() {
        calendarArrowLeftIv = findViewById(R.id.ivCalendarArrowLeft);
        calendarDateTv = findViewById(R.id.tvCalendarDate);
        calendarArrowRightIv = findViewById(R.id.ivCalendarArrowRight);
        emptyAppointmentsTv = findViewById(R.id.tvEmptyAppointments);
        emptyAppointmentsTv.setVisibility(View.GONE);
        chatFab = findViewById(R.id.fabChat);

        RecyclerView appointmentsRv = findViewById(R.id.rvAppointments);
        appointmentAdapter = new AppointmentAdapter(appointmentsList);
        appointmentsRv.setAdapter(appointmentAdapter);
    }

    private void setClickListeners() {
        calendarArrowLeftIv.setOnClickListener(v -> {
            if (selectedDate.equals(LocalDate.now())) {
                return;
            }

            selectedDate = selectedDate.minusDays(1);
            if (selectedDate.equals(LocalDate.now())) {
                calendarArrowLeftIv.setColorFilter(ContextCompat.getColor(this, R.color.black_transparent));
            } else {
                calendarArrowLeftIv.setColorFilter(ContextCompat.getColor(this, R.color.black));
            }
            updateCalendar();
        });
        calendarArrowRightIv.setOnClickListener(v -> {
            selectedDate = selectedDate.plusDays(1);
            calendarArrowLeftIv.setColorFilter(ContextCompat.getColor(this, R.color.black));
            updateCalendar();
        });
        chatFab.setOnClickListener(v -> startActivity(new Intent(this, ChatRoomsActivity.class)));
    }

    private void updateCalendar() {
        calendarDateTv.setText(selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        int size = appointmentsList.size();
        appointmentsList.clear();
        appointmentAdapter.notifyItemRangeRemoved(0, size);

        appointmentsList.addAll(DatabaseManager.getInstance().getAppointmentsByDateForDoctor(UserManager.getInstance().getCurrentDoctor().id, selectedDate));
        appointmentAdapter.notifyItemRangeInserted(0, appointmentsList.size());
        emptyAppointmentsTv.setVisibility(appointmentsList.size() == 0 ? View.VISIBLE : View.GONE);
    }
}