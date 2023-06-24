package com.example.stomabooking.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stomabooking.R;
import com.example.stomabooking.models.ChatMessage;
import com.example.stomabooking.viewholders.ChatMessageViewHolder;

import java.util.ArrayList;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageViewHolder> {
    private final ArrayList<ChatMessage> list;

    public ChatMessageAdapter(ArrayList<ChatMessage> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ChatMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout view = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat_message, parent, false);
        return new ChatMessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatMessageViewHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}