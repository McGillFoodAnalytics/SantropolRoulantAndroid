package com.example.santropolroulant;

public class Event {

    String date;
    Integer cap;
    String slot;
    String type;

    public Event(String date, Integer cap, String slot, String type) {
        this.date = date;
        this.cap = cap;
        this.slot = slot;
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

    public Integer getCap() {
        return cap;
    }

    public void setCap(Integer cap) {
        this.cap = cap;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}