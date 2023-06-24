package com.example.stomabooking.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stomabooking.R;

public class ChatRoomViewHolder extends RecyclerView.ViewHolder {
    private final TextView nameTv;

    public ChatRoomViewHolder(@NonNull View itemView) {
        super(itemView);
        nameTv = itemView.findViewById(R.id.tvName);
    }

    public void setData(String name) {
        nameTv.setText(name);
    }
}
