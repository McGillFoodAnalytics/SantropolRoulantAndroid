package com.example.santropolroulant;

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
}
