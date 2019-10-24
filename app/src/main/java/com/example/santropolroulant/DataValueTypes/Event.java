package com.example.santropolroulant.DataValueTypes;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

public class Event{
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
/*
    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(event_date);
        dest.writeString(event_date_txt);
        dest.writeString(event_id);
        dest.writeString(event_time_start);
        dest.writeString(event_time_end);
        dest.writeString(event_type);
        dest.writeBoolean(is_important_event);
        dest.writeString(is_current);
        dest.writeString(first_name);
        dest.writeString(key);
        dest.writeString(last_name);
        dest.writeString(note);
        dest.writeString(uid);

    }

    public static final Parcelable.Creator<Event> CREATOR
            = new Parcelable.Creator<Event>(){
        public Event createFromParcel(Parcel in){
            return new Event(in);
        }

        public Event[] newArray(int size){
            return new Event[size];
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private Event(Parcel in){
     event_date = in.readInt();
     event_date_txt = in.readString();
     event_id = in.readString();
     event_time_start = in.readString();
     event_time_end =in.readString();
     event_type = in.readString();
     is_important_event = in.readBoolean();
     is_current = in.readString();
     first_name = in.readString();
     key = in.readString();
     last_name = in.readString();
     note = in.readString();
     uid = in.readString();
    }*/
}
