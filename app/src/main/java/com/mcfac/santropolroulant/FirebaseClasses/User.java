package com.example.santropolroulant.FirebaseClasses;

public class User {
    private String address_city;
    private Long address_number;
    private String address_postal_code;
    private String address_street;
    private String dob;
    private String email;
    private String first_name;
    private String key;
    private String last_name;
    private Long no_show;
    private String phone_number;
    private String signup_date;
    private String uid;



    public User() {
    }

    public String getAddress_city() {
        return address_city;
    }

    public Long getAddress_number() {
        return address_number;
    }

    public String getAddress_postal_code() {
        return address_postal_code;
    }

    public String getAddress_street() {
        return address_street;
    }

    public String getDob() {
        return dob;
    }

    public String getEmail() {
        return email;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getKey() {
        return key;
    }

    public String getLast_name() {
        return last_name;
    }

    public Long getNo_show() {
        return no_show;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getSignup_date() {
        return signup_date;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

}
