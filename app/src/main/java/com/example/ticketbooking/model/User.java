package com.example.ticketbooking.model;

public class User {
    private String profilePic;
    private String userId;
    private String username;
    private String email;
    private String password;

    public User(String profilePic, String userId, String username, String email, String password) {
        this.userId = userId;
        this.profilePic = profilePic;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getUserId() {
        return userId;
    }


}
