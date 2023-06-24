package com.example.stomabooking.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stomabooking.R;
import com.example.stomabooking.models.Appointment;
import com.example.stomabooking.viewholders.AppointmentViewHolder;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentViewHolder> {
    public interface AppointmentCalendarItemClick {
        void onDelete(int position);
    }

    private final ArrayList<Appointment> list;
    private final AppointmentCalendarItemClick appointmentCalendarItemClick;

    public AppointmentAdapter(ArrayList<Appointment> list) {
        this.list = list;
        appointmentCalendarItemClick = null;
    }

    public AppointmentAdapter(ArrayList<Appointment> list, AppointmentCalendarItemClick appointmentCalendarItemClick) {
        this.list = list;
        this.appointmentCalendarItemClick = appointmentCalendarItemClick;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MaterialCardView view = (MaterialCardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.row_appointment, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        holder.setData(list.get(position));
        if (appointmentCalendarItemClick == null) {
            holder.getDeleteButton().setVisibility(View.GONE);
        } else {
            holder.getDeleteButton().setOnClickListener(v -> appointmentCalendarItemClick.onDelete(holder.getAdapterPosition()));
            holder.getDeleteButton().setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}