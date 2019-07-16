package com.example.santropolroulant.FirebaseClasses;

public class UserProfile {
    public String Email;
    public String FirstName;
    public String LastName;

    public UserProfile() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public UserProfile(String userEmail, String userFirstName, String userLastName){
        this.Email = userEmail;
        this.FirstName = userFirstName;
        this.LastName = userLastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
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
