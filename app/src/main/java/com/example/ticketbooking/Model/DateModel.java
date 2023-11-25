package com.example.ticketbooking.Model;

public class DateModel {
    private String date;
    private boolean isSelected;

    public DateModel(String date, boolean isSelected) {
        this.date = date;
        this.isSelected = isSelected;
    }

    public String getDate() {
        return date;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
