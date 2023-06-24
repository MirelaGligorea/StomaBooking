package com.example.stomabooking.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stomabooking.models.User;
import com.example.stomabooking.viewholders.AssociatedUserViewHolder;
import com.example.stomabooking.R;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class AssociatedUserAdapter extends RecyclerView.Adapter<AssociatedUserViewHolder> {
    public interface AssociatedUserItemClick {
        void onDelete(User user, int position);
    }

    private final ArrayList<User> list;
    private final AssociatedUserItemClick associatedUserItemClick;

    public AssociatedUserAdapter(ArrayList<User> list, AssociatedUserItemClick associatedUserItemClick) {
        this.list = list;
        this.associatedUserItemClick = associatedUserItemClick;
    }

    @NonNull
    @Override
    public AssociatedUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MaterialCardView view = (MaterialCardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.row_associated_user, parent, false);
        return new AssociatedUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssociatedUserViewHolder holder, int position) {
        User user = list.get(position);
        holder.setData(user);
        holder.getDeleteIv().setOnClickListener(v -> associatedUserItemClick.onDelete(user, holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}