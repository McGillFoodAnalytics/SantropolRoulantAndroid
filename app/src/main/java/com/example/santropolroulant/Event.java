package com.example.santropolroulant;

public class Event {

    String date;
    String type;


    public Event(String date, String type, Integer cap, String slot) {
        this.date = date;
        this.type = type;

    }

    public Event() {
        ;
    } // Empty constructor

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}