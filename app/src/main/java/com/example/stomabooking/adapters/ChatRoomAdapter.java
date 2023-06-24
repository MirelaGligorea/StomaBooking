package com.example.stomabooking.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stomabooking.R;
import com.example.stomabooking.viewholders.ChatRoomViewHolder;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomViewHolder> {
    public interface ChatRoomItemClick {
        void onClick(int position);
    }

    private final ArrayList<String> list;
    private final ChatRoomItemClick chatRoomItemClick;

    public ChatRoomAdapter(ArrayList<String> list, ChatRoomItemClick chatRoomItemClick) {
        this.list = list;
        this.chatRoomItemClick = chatRoomItemClick;
    }

    @NonNull
    @Override
    public ChatRoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MaterialCardView view = (MaterialCardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chat_room, parent, false);
        return new ChatRoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatRoomViewHolder holder, int position) {
        holder.setData(list.get(position));
        holder.itemView.setOnClickListener(v -> chatRoomItemClick.onClick(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}