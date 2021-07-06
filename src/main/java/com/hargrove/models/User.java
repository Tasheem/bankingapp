package com.hargrove.models;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;
import com.hargrove.enums.Gender;
import com.hargrove.enums.PreferredPronoun;

public class User {
    private UUID id;
    private String firstName, lastName, email, username, password;
    Gender gender;
    PreferredPronoun preferredPronoun;
    // Dates are stored as strings in database.
    private LocalDate birthday;
    // java.time.Instant represents date in UTC, or an offset of +/-0000.
    private Instant dateOfAccountCreation;
    private Role role;

    public User() {}

    public User(UUID id, Instant dateOfAccountCreation) {
        this.id = id;
        this.dateOfAccountCreation = dateOfAccountCreation;
    }

    public UUID getID() {
        return this.id;
    }

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

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public PreferredPronoun getPreferredPronoun() {
        return preferredPronoun;
    }
    public void setPreferredPronoun(PreferredPronoun preferredPronoun) {
        this.preferredPronoun = preferredPronoun;
    }

    public LocalDate getBirthday() {
        return birthday;
    }
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Instant getDateOfAccountCreation() {
        return dateOfAccountCreation;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
