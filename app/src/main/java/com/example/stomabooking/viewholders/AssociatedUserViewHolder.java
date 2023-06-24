package com.example.stomabooking.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stomabooking.R;
import com.example.stomabooking.models.User;

public class AssociatedUserViewHolder extends RecyclerView.ViewHolder {
    private final ImageView deleteIv;
    private final TextView nameTv;
    private final TextView birthdateTv;

    public AssociatedUserViewHolder(@NonNull View itemView) {
        super(itemView);
        deleteIv = itemView.findViewById(R.id.ivDelete);
        nameTv = itemView.findViewById(R.id.tvName);
        birthdateTv = itemView.findViewById(R.id.tvBirthdate);
    }

    public ImageView getDeleteIv() {
        return deleteIv;
    }

    public void setData(User user) {
        nameTv.setText(user.firstName + " " + user.lastName);
        birthdateTv.setText(user.birthdate);
    }
}
