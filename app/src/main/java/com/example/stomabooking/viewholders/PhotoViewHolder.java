package com.example.stomabooking.viewholders;

import android.net.Uri;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.stomabooking.R;

public class PhotoViewHolder extends RecyclerView.ViewHolder {
    private final ImageView photoIv;
    private final FrameLayout deleteFl;

    public PhotoViewHolder(@NonNull View itemView) {
        super(itemView);
        photoIv = itemView.findViewById(R.id.ivPhoto);
        deleteFl = itemView.findViewById(R.id.flDelete);
    }

    public FrameLayout getDeleteContainer() {
        return deleteFl;
    }

    public void setPhoto(Uri photoUri) {
        Glide.with(photoIv.getContext()).load(photoUri).into(photoIv);
    }
}
