package com.hargrove.builders;

import com.hargrove.enums.Gender;
import com.hargrove.enums.PreferredPronoun;
import com.hargrove.models.Role;
import com.hargrove.models.User;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class UserBuilder {
    private UUID id;
    private String firstName, lastName, email, username, password;
    Gender gender;
    PreferredPronoun preferredPronoun;
    // Will be stored as strings in database.
    // java.time.Instant represents date in UTC, or an offset of +/-0000.
    private LocalDate birthday;
    private Instant dateOfAccountCreation;
    private Role role;

    public UserBuilder setId(UUID id) {
        this.id = id;
        return this;
    }

    public UserBuilder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserBuilder setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder setGender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public UserBuilder setPreferredPronoun(PreferredPronoun preferredPronoun) {
        this.preferredPronoun = preferredPronoun;
        return this;
    }

    public UserBuilder setBirthday(LocalDate birthday) {
        this.birthday = birthday;
        return this;
    }

    public UserBuilder setDateOfAccountCreation(Instant dateOfAccountCreation) {
        this.dateOfAccountCreation = dateOfAccountCreation;
        return this;
    }

    public UserBuilder setRole(Role role) {
        this.role = role;
        return this;
    }

    public User build() {
        User user = new User(id, dateOfAccountCreation);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        user.setGender(gender);
        user.setPreferredPronoun(preferredPronoun);
        user.setBirthday(birthday);
        user.setRole(role);

        return user;
    }
}
