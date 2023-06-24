package com.example.stomabooking.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
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

public class MainActivity extends AppCompatActivity {
    private TextView profileTv;
    private TextView appointmentsHistoryTv;
    private ImageView calendarArrowLeftIv;
    private TextView calendarDateTv;
    private ImageView calendarArrowRightIv;
    private TextView emptyAppointmentsTv;
    private FloatingActionButton chatFab;
    private FloatingActionButton addAppointmentFab;

    private LocalDate selectedDate = LocalDate.now();
    private final ArrayList<Appointment> appointmentsList = new ArrayList<>();
    private AppointmentAdapter appointmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setClickListeners();
        updateCalendar();
        calendarArrowLeftIv.setColorFilter(ContextCompat.getColor(this, R.color.black_transparent));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AddAppointmentActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            new Handler().postDelayed(this::updateCalendar, 500);
        }
    }

    private void initViews() {
        profileTv = findViewById(R.id.tvProfile);
        appointmentsHistoryTv = findViewById(R.id.tvAppointmentsHistory);
        calendarArrowLeftIv = findViewById(R.id.ivCalendarArrowLeft);
        calendarDateTv = findViewById(R.id.tvCalendarDate);
        calendarArrowRightIv = findViewById(R.id.ivCalendarArrowRight);
        emptyAppointmentsTv = findViewById(R.id.tvEmptyAppointments);
        emptyAppointmentsTv.setVisibility(View.GONE);
        chatFab = findViewById(R.id.fabChat);
        addAppointmentFab = findViewById(R.id.fabAddAppointment);

        RecyclerView appointmentsRv = findViewById(R.id.rvAppointments);
        appointmentAdapter = new AppointmentAdapter(appointmentsList, (position) -> {
            Appointment appointment = appointmentsList.get(position);
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.delete_appointment_title))
                    .setMessage(String.format(getString(R.string.delete_appointment_message), appointment.title, appointment.patientName))
                    .setCancelable(true)
                    .setPositiveButton(getString(R.string.delete), (dialog, id) -> {
                        DatabaseManager.getInstance().removeAppointment(appointment);
                        appointmentsList.remove(appointment);
                        appointmentAdapter.notifyItemRemoved(position);
                        emptyAppointmentsTv.setVisibility(appointmentsList.size() == 0 ? View.VISIBLE : View.GONE);
                        dialog.dismiss();
                    })
                    .setNegativeButton(getString(R.string.cancel), (dialog, id) -> dialog.dismiss())
                    .create().show();
        });
        appointmentsRv.setAdapter(appointmentAdapter);
    }

    private void setClickListeners() {
        profileTv.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
        appointmentsHistoryTv.setOnClickListener(v -> startActivity(new Intent(this, AppointmentsHistoryActivity.class)));
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
        addAppointmentFab.setOnClickListener(v -> startActivityForResult(
                new Intent(this, AddAppointmentActivity.class), AddAppointmentActivity.REQUEST_CODE));
    }

    private void updateCalendar() {
        calendarDateTv.setText(selectedDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        int size = appointmentsList.size();
        appointmentsList.clear();
        appointmentAdapter.notifyItemRangeRemoved(0, size);

        appointmentsList.addAll(DatabaseManager.getInstance().getAppointmentsByDateForUser(UserManager.getInstance().getCurrentUser().id, selectedDate));
        appointmentAdapter.notifyItemRangeInserted(0, appointmentsList.size());
        emptyAppointmentsTv.setVisibility(appointmentsList.size() == 0 ? View.VISIBLE : View.GONE);
    }
}