package com.example.stomabooking.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stomabooking.R;
import com.example.stomabooking.models.SelectableText;
import com.example.stomabooking.viewholders.SelectableTextViewHolder;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class SelectableTextAdapter extends RecyclerView.Adapter<SelectableTextViewHolder> {
    public interface SelectableTextItemClick {
        void onClick(SelectableText selectedItem);
    }

    private final ArrayList<SelectableText> list;
    private final SelectableTextItemClick selectableTextItemClick;

    public SelectableTextAdapter(ArrayList<SelectableText> list, SelectableTextItemClick selectableTextItemClick) {
        this.list = list;
        this.selectableTextItemClick = selectableTextItemClick;
    }

    @NonNull
    @Override
    public SelectableTextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MaterialCardView view = (MaterialCardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.row_selectable_text, parent, false);
        return new SelectableTextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectableTextViewHolder holder, int position) {
        SelectableText selectableText = list.get(position);
        holder.setData(selectableText);
        holder.getContainer().setOnClickListener(v -> selectableTextItemClick.onClick(selectableText));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}