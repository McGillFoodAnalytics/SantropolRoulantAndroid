package com.example.santropolroulant.DataValueTypes;

public class Event {
    private int event_date;
    private String event_date_txt;
    private String event_id;
    private String event_time_end;
    private String event_time_start;
    private String event_type;
    private String first_name;
    private boolean first_shift;
    private String is_current;
    private boolean is_important_event;
    private String key;
    private String last_name;
    private String note;
    private String uid;

    public Event(){
    }

    public Event(int event_date, String event_date_txt, String event_id) {
        this.event_date = event_date;
        this.event_date_txt = event_date_txt;
        this.event_id = event_id;
    }

    public int getEvent_date() {
        return event_date;
    }

    public String getEvent_date_txt() {
        return event_date_txt;
    }

    public String getEvent_id() {
        return event_id;
    }

    public String getEvent_time_end() {
        return event_time_end;
    }

    public String getEvent_time_start() {
        return event_time_start;
    }

    public String getEvent_type() {
        return event_type;
    }

    public String getFirst_name() {
        return first_name;
    }

    public boolean isFirst_shift() {
        return first_shift;
    }

    public String getIs_current() {
        return is_current;
    }

    public boolean isIs_important_event() {
        return is_important_event;
    }

    public String getKey() {
        return key;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getNote() {
        return note;
    }

    public String getUid() {
        return uid;
    }
}
