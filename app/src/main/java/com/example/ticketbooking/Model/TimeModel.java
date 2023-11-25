package com.example.ticketbooking.Model;

public class TimeModel {
    private String time;
    private boolean isSelected;

    public TimeModel(String time, boolean isSelected) {
        this.time = time;
        this.isSelected = isSelected;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
