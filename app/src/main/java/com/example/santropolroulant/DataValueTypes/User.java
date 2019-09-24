package com.example.santropolroulant.DataValueTypes;

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

    public User(){}

    public User(String address_city, Long address_number, String address_postal_code, String address_street, String dob, String email, String first_name, String key, String last_name, Long no_show, String phone_number, String signup_date) {
        this.address_city = address_city;
        this.address_number = address_number;
        this.address_postal_code = address_postal_code;
        this.address_street = address_street;
        this.dob = dob;
        this.email = email;
        this.first_name = first_name;
        this.key = key;
        this.last_name = last_name;
        this.no_show = no_show;
        this.phone_number = phone_number;
        this.signup_date = signup_date;
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
}
