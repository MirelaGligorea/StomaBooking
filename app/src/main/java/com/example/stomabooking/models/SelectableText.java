package com.example.stomabooking.models;

public class SelectableText {
    private String text;
    private boolean selected;

    public SelectableText(String text) {
        this.text = text;
        this.selected = false;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
