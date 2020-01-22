package com.example.santropolroulant.FirebaseClasses;

public class Event {

    String date_txt;
    Integer date;
    String start_time;
    String end_time;
    String event_type;
    String uid;
    String note;
    Boolean first_shift;
    Boolean is_current;
    String event_id;


    public Event(String date_txt, Integer date, String start_time, String end_time, String event_type, String uid, String note, Boolean is_current, Boolean first_shift, String event_id) {

        this.date = date;
        this.date_txt = date_txt;
        this.start_time = start_time;
        this.end_time = end_time;
        this.event_type = event_type;
        this.uid = uid;
        this.note = note;
        this.first_shift = first_shift;
        this.is_current = is_current;
        this.event_id = event_id;
    }

    public Event() {
        ;
    } // Empty constructor

    public String getDate_txt() {
        return date_txt;
    }

    public void setDate_txt(String date_txt) {
        this.date_txt = date_txt;
    }

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getFirst_shift() {
        return first_shift;
    }

    public void setFirst_shift(Boolean first_shift) {
        this.first_shift = first_shift;
    }

    public Boolean getIs_current() {
        return is_current;
    }

    public void setIs_current(Boolean is_current) {
        this.is_current = is_current;
    }

    public String getEventId() { return event_id; }
}