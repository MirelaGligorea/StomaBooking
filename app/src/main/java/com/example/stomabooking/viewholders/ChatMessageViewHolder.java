package com.example.stomabooking.viewholders;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stomabooking.R;
import com.example.stomabooking.managers.UserManager;
import com.example.stomabooking.models.ChatMessage;
import com.google.android.material.card.MaterialCardView;

public class ChatMessageViewHolder extends RecyclerView.ViewHolder {
    private final MaterialCardView mcvContainer;
    private final TextView headerTv;
    private final TextView messageTv;

    public ChatMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        mcvContainer = itemView.findViewById(R.id.mcvContainer);
        headerTv = itemView.findViewById(R.id.tvHeader);
        messageTv = itemView.findViewById(R.id.tvMessage);
    }

    public void setData(ChatMessage chatMessage) {
        Context context = itemView.getContext();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int smallMargin = (int)((24 * displayMetrics.density) + 0.5);
        int bigMargin = (int)((70 * displayMetrics.density) + 0.5);
        int backgroundColor = chatMessage.isFromDoctor ? R.color.pink : R.color.white;
        int textColor = chatMessage.isFromDoctor ? R.color.white : R.color.black;
        String name = chatMessage.isFromDoctor ? chatMessage.doctorName : context.getString(R.string.you);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)mcvContainer.getLayoutParams();
        params.setMarginStart(chatMessage.isFromDoctor ? smallMargin : bigMargin);
        params.setMarginEnd(chatMessage.isFromDoctor ? bigMargin : smallMargin);
        params.gravity = chatMessage.isFromDoctor ? Gravity.START : Gravity.END;

        if (UserManager.getInstance().getCurrentDoctor() != null) {
            name = chatMessage.isFromDoctor ? context.getString(R.string.you) : chatMessage.userName;
            params.setMarginStart(chatMessage.isFromDoctor ? bigMargin : smallMargin);
            params.setMarginEnd(chatMessage.isFromDoctor ? smallMargin : bigMargin);
            params.gravity = chatMessage.isFromDoctor ? Gravity.END : Gravity.START;
        }
        mcvContainer.setLayoutParams(params);

        mcvContainer.setCardBackgroundColor(ContextCompat.getColor(context, backgroundColor));
        headerTv.setTextColor(ContextCompat.getColor(context, textColor));
        headerTv.setText(name + ", " + chatMessage.getDateAndTime());
        messageTv.setTextColor(ContextCompat.getColor(context, textColor));
        messageTv.setText(chatMessage.message);
    }
}
