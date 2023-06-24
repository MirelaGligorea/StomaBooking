package com.example.stomabooking.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stomabooking.R;
import com.example.stomabooking.adapters.AppointmentAdapter;
import com.example.stomabooking.managers.DatabaseManager;
import com.example.stomabooking.managers.UserManager;
import com.example.stomabooking.models.Appointment;

import java.util.ArrayList;

public class AppointmentsHistoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments_history);
        initViews();
    }

    private void initViews() {
        ImageButton backIb = findViewById(R.id.ibBack);
        backIb.setOnClickListener(v -> onBackPressed());

        ArrayList<Appointment> appointmentsList = DatabaseManager.getInstance().getAppointmentsHistory(UserManager.getInstance().getCurrentUser().id);
        RecyclerView appointmentsRv = findViewById(R.id.rvAppointments);
        appointmentsRv.setAdapter(new AppointmentAdapter(appointmentsList));

        TextView emptyAppointmentsTv = findViewById(R.id.tvEmptyAppointments);
        emptyAppointmentsTv.setVisibility(appointmentsList.size() == 0 ? View.VISIBLE : View.GONE);
    }
}