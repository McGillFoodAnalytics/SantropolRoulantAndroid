package com.example.santropolroulant.FirebaseClasses;

public class Event {

    String date;
    Integer cap;
    String slot;
    String type;
    String eid;
    Integer num_vols;

    public Event(String date, Integer cap, String slot, String type, String eid, Integer num_vols) {
        this.date = date;
        this.cap = cap;
        this.slot = slot;
        this.type = type;
        this.eid = eid;
        this.num_vols = num_vols;
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

    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public Integer getNum_vols() {
        return num_vols;
    }

    public void setNum_vols(Integer num_vols) {
        this.num_vols = num_vols;
    }
}