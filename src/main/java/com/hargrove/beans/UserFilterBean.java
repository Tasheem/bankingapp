package com.hargrove.beans;

import jakarta.ws.rs.QueryParam;

public class UserFilterBean {
    private @QueryParam("firstname") String firstName;
    private @QueryParam("lastname") String lastName;
    private @QueryParam("password") String password;
    private @QueryParam("newPassword") String newPassword;
    private @QueryParam("email") String email;
    private @QueryParam("username") String username;
    private  @QueryParam("birthday") String birthday;
    private @QueryParam("gender") String gender;
    private @QueryParam("preferredPronoun") String preferredPronoun;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPreferredPronoun() {
        return preferredPronoun;
    }

    public void setPreferredPronoun(String preferredPronoun) {
        this.preferredPronoun = preferredPronoun;
    }
}
