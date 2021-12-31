package com.hargrove.services;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import com.hargrove.builders.UserBuilder;
import com.hargrove.dao.UserDAO;
import com.hargrove.enums.Gender;
import com.hargrove.enums.PreferredPronoun;
import com.hargrove.models.Role;
import com.hargrove.models.User;

public class UserService {
    private UserDAO db;
    private RoleService roleService;
    private User user;

    public User getUser(UUID id) {
        db = new UserDAO();
        User user = db.queryUser(id);

        return user;
    }
    public User getUser(String username, String password) {
        db = new UserDAO();
        User user = db.queryUser(username, password);

        return user;
    }

    public List<User> getAllUsers() {
        db = new UserDAO();
        List<User> users = db.queryAllUsers();

        return users;
    }

    /*
    * New user object has to be created here instead of passing the user from the UserResource
    * because the id and date of creation have to be created when the object in instantiated.
    * These fields are read-only.
     */
    public User createUser(List<String> stringFields, List<Enum<?>> genderInfo, LocalDate birthday) {
        db = new UserDAO();
        UUID id = UUID.randomUUID();

        Instant currentDateTimeUTC = Instant.now();

        roleService = new RoleService();
        // User creating their own account is given customer role by default.
        Role role = roleService.getRole("ROLE_CUSTOMER");

        user = new UserBuilder()
                .setId(id)
                .setDateOfAccountCreation(currentDateTimeUTC)
                .setFirstName(stringFields.get(0))
                .setLastName(stringFields.get(1))
                .setEmail(stringFields.get(2))
                .setUsername(stringFields.get(3))
                .setPassword(stringFields.get(4))
                .setGender((Gender)genderInfo.get(0))
                .setPreferredPronoun((PreferredPronoun)genderInfo.get(1))
                .setBirthday(birthday)
                .setRole(role)
                .build();

        db.saveUser(user);

        return user;
    }

    public User createUser(List<String> stringFields, List<Enum<?>> genderInfo, LocalDate birthday,
                            String roleName) {
        db = new UserDAO();
        UUID id = UUID.randomUUID();

        Instant currentDateTimeUTC = Instant.now();

        roleService = new RoleService();
        Role role = roleService.getRole(roleName);

        user = new UserBuilder()
                .setId(id)
                .setDateOfAccountCreation(currentDateTimeUTC)
                .setFirstName(stringFields.get(0))
                .setLastName(stringFields.get(1))
                .setEmail(stringFields.get(2))
                .setUsername(stringFields.get(3))
                .setPassword(stringFields.get(4))
                .setGender((Gender)genderInfo.get(0))
                .setPreferredPronoun((PreferredPronoun)genderInfo.get(1))
                .setBirthday(birthday)
                .setRole(role)
                .build();

        db.saveUser(user);

        return user;
    }

    public boolean deleteUser(UUID userID) {
        db = new UserDAO();
        boolean deletionSuccessful = db.deleteUser(userID.toString());

        return deletionSuccessful;
    }

    public void updateFirstName(String uuid, String firstName) {
        db = new UserDAO();
        db.updateFirstName(uuid, firstName);
    }

    public void updateLastName(String uuid, String lastName) {
        db = new UserDAO();
        db.updateLastName(uuid, lastName);
    }

    public void updateName(String uuid, String firstName, String lastName) {
        db = new UserDAO();
        db.updateName(uuid, firstName, lastName);
    }

    public void updatePassword(String uuid, String newPassword) {
        db = new UserDAO();
        db.updatePassword(uuid, newPassword);
    }

    public void updateUsername(String uuid, String username) {
        db = new UserDAO();
        db.updateUsername(uuid, username);
    }

    public void updateEmail(String uuid, String email) {
        db = new UserDAO();
        db.updateEmail(uuid, email);
    }

    public void updateBirthday(String uuid, String birthday) {
        db = new UserDAO();
        db.updateBirthday(uuid, birthday);
    }

    public void updateGender(String uuid, String gender, String preferredPronoun) {
        db = new UserDAO();
        Gender g = null;
        PreferredPronoun pn = null;
        try {
            g = Gender.valueOf(gender.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid Gender");
            System.out.println(e.getMessage());
        }

        if(!preferredPronoun.equals("null")) {
            try {
                pn = PreferredPronoun.valueOf(preferredPronoun.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid Preferred Pronoun.");
                System.out.println(e.getMessage());
            }
        }
        db.updateGender(uuid, g, pn);
    }
}
