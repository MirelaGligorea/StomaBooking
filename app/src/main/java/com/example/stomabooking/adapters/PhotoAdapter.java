package com.example.stomabooking.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stomabooking.R;
import com.example.stomabooking.viewholders.PhotoViewHolder;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoViewHolder> {
    public interface PhotoItemClick {
        void onDelete(int position);
    }

    private final ArrayList<Uri> list;
    private final PhotoItemClick photoItemClick;

    public PhotoAdapter(ArrayList<Uri> list) {
        this.list = list;
        photoItemClick = null;
    }

    public PhotoAdapter(ArrayList<Uri> list, PhotoItemClick photoItemClick) {
        this.list = list;
        this.photoItemClick = photoItemClick;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MaterialCardView view = (MaterialCardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.row_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        holder.setPhoto(list.get(position));
        if (photoItemClick == null) {
            holder.getDeleteContainer().setVisibility(View.GONE);
        } else {
            holder.getDeleteContainer().setOnClickListener(v -> photoItemClick.onDelete(holder.getAdapterPosition()));
            holder.getDeleteContainer().setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}