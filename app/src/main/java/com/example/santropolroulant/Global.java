package com.example.santropolroulant;

import android.app.Application;

public class Global extends Application {
    private String data= "KitchenAm";

    public String getData(){
        return this.data;
    }

    public void setData(String d){
        this.data=d;
    }
}
