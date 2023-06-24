package com.example.stomabooking.viewholders;

import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stomabooking.R;
import com.example.stomabooking.adapters.PhotoAdapter;
import com.example.stomabooking.models.Appointment;
import com.example.stomabooking.models.AppointmentImage;

import java.util.ArrayList;

public class AppointmentViewHolder extends RecyclerView.ViewHolder {
    private final TextView serviceTv;
    private final TextView dateAndTimeTv;
    private final TextView durationTv;
    private final TextView doctorNameTv;
    private final TextView patientNameTv;
    private final TextView priceTv;
    private final Button deleteBtn;
    private final ArrayList<Uri> photoUriList = new ArrayList<>();
    private final PhotoAdapter photoAdapter;

    public AppointmentViewHolder(@NonNull View itemView) {
        super(itemView);
        serviceTv = itemView.findViewById(R.id.tvService);
        dateAndTimeTv = itemView.findViewById(R.id.tvDateAndTime);
        durationTv = itemView.findViewById(R.id.tvDuration);
        doctorNameTv = itemView.findViewById(R.id.tvDoctorName);
        patientNameTv = itemView.findViewById(R.id.tvPatientName);
        priceTv = itemView.findViewById(R.id.tvPrice);
        deleteBtn = itemView.findViewById(R.id.btnDelete);

        RecyclerView photoRv = itemView.findViewById(R.id.rvPhotos);
        photoAdapter = new PhotoAdapter(photoUriList);
        photoRv.setAdapter(photoAdapter);
    }

    public Button getDeleteButton() {
        return deleteBtn;
    }

    public void setData(Appointment appointment) {
        serviceTv.setText(appointment.title);
        dateAndTimeTv.setText(appointment.date + ", " + appointment.startHour + "-" + appointment.endHour);
        durationTv.setText(appointment.duration + " min");
        doctorNameTv.setText(appointment.doctorName);
        patientNameTv.setText(appointment.patientName);
        priceTv.setText(appointment.price + " lei");

        int size = photoUriList.size();
        photoUriList.clear();
        photoAdapter.notifyItemRangeRemoved(0, size);
        for (AppointmentImage appointmentImage : appointment.images) {
            photoUriList.add(Uri.parse(appointmentImage.url));
        }
        photoAdapter.notifyItemRangeInserted(0, photoUriList.size());
    }
}
