package com.example.santropolroulant.FirebaseClasses;

public class Event {

    private int event_date;
    private String event_date_txt;
    private String event_id;
    private String event_time_end;
    private String event_time_start;
    private String event_type;
    private String first_name;
    private boolean first_shift;
    private boolean is_current;
    private boolean is_important_event;
    private String key;
    private String last_name;
    private String note;
    private String slot;
    private String uid;

    public int getEvent_date() {
        return event_date;
    }

    public void setEvent_date(int event_date) {
        this.event_date = event_date;
    }

    public String getEvent_date_txt() {
        return event_date_txt;
    }

    public void setEvent_date_txt(String event_date_txt) {
        this.event_date_txt = event_date_txt;
    }

    public String getEvent_time_end() {
        return event_time_end;
    }

    public void setEvent_time_end(String event_time_end) {
        this.event_time_end = event_time_end;
    }

    public String getEvent_time_start() {
        return event_time_start;
    }

    public void setEvent_time_start(String event_time_start) {
        this.event_time_start = event_time_start;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public boolean isFirst_shift() {
        return first_shift;
    }

    public void setFirst_shift(boolean first_shift) {
        this.first_shift = first_shift;
    }

    public boolean isIs_current() {
        return is_current;
    }

    public void setIs_current(boolean is_current) {
        this.is_current = is_current;
    }

    public boolean isIs_important_event() {
        return is_important_event;
    }

    public void setIs_important_event(boolean is_important_event) {
        this.is_important_event = is_important_event;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}