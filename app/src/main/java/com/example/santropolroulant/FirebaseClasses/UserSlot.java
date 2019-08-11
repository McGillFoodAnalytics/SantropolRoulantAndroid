package com.example.santropolroulant.FirebaseClasses;

public class UserSlot {
    public String NumVal;
    public String FirstName;
    public String LastName;
    public String Key;

    public UserSlot() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public UserSlot(String numVal, String userFirstName, String userLastName, String Key){
        this.NumVal = numVal;
        this.FirstName = userFirstName;
        this.LastName = userLastName;
        this.Key = Key;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getNumVal() {
        return NumVal;
    }

    public void setNumVal(String numVal) {
        NumVal = numVal;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }
}
