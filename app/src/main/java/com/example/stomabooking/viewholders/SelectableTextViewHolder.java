package com.example.stomabooking.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stomabooking.R;
import com.example.stomabooking.models.SelectableText;
import com.google.android.material.card.MaterialCardView;

public class SelectableTextViewHolder extends RecyclerView.ViewHolder {
    private final MaterialCardView mcvContainer;
    private final TextView textTv;

    public SelectableTextViewHolder(@NonNull View itemView) {
        super(itemView);
        mcvContainer = itemView.findViewById(R.id.mcvContainer);
        textTv = itemView.findViewById(R.id.tvText);
    }

    public MaterialCardView getContainer() {
        return mcvContainer;
    }

    public void setData(SelectableText selectableText) {
        int backgroundColor = selectableText.isSelected() ? R.color.pink : R.color.white;
        int textColor = selectableText.isSelected() ? R.color.white : R.color.black;

        mcvContainer.setCardBackgroundColor(ContextCompat.getColor(mcvContainer.getContext(), backgroundColor));
        textTv.setTextColor(ContextCompat.getColor(mcvContainer.getContext(), textColor));
        textTv.setText(selectableText.getText());
    }
}
