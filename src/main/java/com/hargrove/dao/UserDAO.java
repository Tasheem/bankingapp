package com.hargrove.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.hargrove.builders.UserBuilder;
import com.hargrove.enums.Gender;
import com.hargrove.enums.PreferredPronoun;
import com.hargrove.models.Role;
import com.hargrove.models.User;
import com.hargrove.utilities.JDBCUtil;

public class UserDAO {
    private JDBCUtil jdbc;

    private User executeQuery(String query) {
        User user = null;

        try {
            jdbc = new JDBCUtil();
            jdbc.establishConnection();
            Statement st = jdbc.getConnection().createStatement();
            ResultSet rs = st.executeQuery(query);
            // Moving pointer from top of table.
            rs.next();
            /*
             * Database user table columns:
             * column 1 - 11
             * id, first_name, last_name, email, username, password, gender, preferredPronoun,
             * birthday, account_creation, role_id
             *
             * Inner join adds columns 12 and 13.
             */
            UUID id = UUID.fromString(rs.getString(1));
            String utc = rs.getString(10);
            Instant accountCreation = Instant.parse(utc);

            Gender gender = Gender.valueOf(rs.getString(7).toUpperCase());

            LocalDate birthday = LocalDate.parse(rs.getString(9));

            String roleID = rs.getString(12);
            UUID roleUUID = UUID.fromString(roleID);
            String roleName = rs.getString(13);

            Role role = new Role(roleUUID, roleName);

            user = new UserBuilder()
                    .setId(id)
                    .setDateOfAccountCreation(accountCreation)
                    .setFirstName(rs.getString(2))
                    .setLastName(rs.getString(3))
                    .setEmail(rs.getString(4))
                    .setUsername(rs.getString(5))
                    .setPassword(rs.getString(6))
                    .setGender(gender)
                    .setBirthday(birthday)
                    .setRole(role)
                    .build();

            if(rs.getString(8) != null) {
                PreferredPronoun pronoun = PreferredPronoun
                        .valueOf(rs.getString(8).toUpperCase());
                user.setPreferredPronoun(pronoun);
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException);
        } finally {
            jdbc.closeConnection();
        }

        return user;
    }

    public User queryUser(UUID id) {
        String query = String.format("SELECT users.*, roles.id, roles.name " +
                        "FROM users " +
                        "INNER JOIN roles ON users.role_id = roles.id " +
                        "WHERE users.id = \"%s\";", id);

        User user = executeQuery(query);

        return user;
    }

    public User queryUser(String username, String password) {
        String query = String.format("SELECT users.*, roles.id, roles.name " +
                        "FROM users " +
                        "INNER JOIN roles ON users.role_id = roles.id " +
                        "WHERE username = \"%s\" AND password = \"%s\";",
                username, password);

        User user = executeQuery(query);

        return user;
    }

    public List<User> queryAllUsers() {
        String query = "SELECT users.*, roles.id, roles.name " +
                        "FROM users " +
                        "INNER JOIN roles ON users.role_id = roles.id;";
        List<User> users = new ArrayList<User>();

        try {
            jdbc = new JDBCUtil();
            jdbc.establishConnection();
            Statement st = jdbc.getConnection().createStatement();
            ResultSet rs = st.executeQuery(query);

            while(rs.next()) {
                /*
                 * Database user table columns:
                 *
                 * id, first_name, last_name, email, username, password, gender, preferredPronoun,
                 * birthday, account_creation
                 */
                UUID id = UUID.fromString(rs.getString(1));
                String utc = rs.getString(10);
                Instant accountCreation = Instant.parse(utc);
                Gender gender = Gender.valueOf(rs.getString(7).toUpperCase());

                LocalDate birthday = LocalDate.parse(rs.getString(9));

                String roleID = rs.getString(12);
                UUID roleUUID = UUID.fromString(roleID);
                String roleName = rs.getString(13);

                Role role = new Role(roleUUID, roleName);

                User user = new UserBuilder()
                        .setId(id)
                        .setDateOfAccountCreation(accountCreation)
                        .setFirstName(rs.getString(2))
                        .setLastName(rs.getString(3))
                        .setEmail(rs.getString(4))
                        .setUsername(rs.getString(5))
                        .setPassword(rs.getString(6))
                        .setGender(gender)
                        .setBirthday(birthday)
                        .setRole(role)
                        .build();

                if(rs.getString(8) != null) {
                    PreferredPronoun pronoun = PreferredPronoun
                            .valueOf(rs.getString(8).toUpperCase());
                    user.setPreferredPronoun(pronoun);
                }
                users.add(user);
            }

        } catch(SQLException sqlEx) {
            System.out.println(sqlEx);
        } finally {
            jdbc.closeConnection();
        }


        return users;
    }

    public void saveUser(User user) {
        /*
         * Database user table columns:
         *
         * id, first_name, last_name, email, username, password, gender, preferredPronoun,
         * birthday, account_creation, role_id
         */
        String insert = "INSERT INTO users Values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try {
            jdbc = new JDBCUtil();
            jdbc.establishConnection();
            PreparedStatement ps = jdbc.getConnection().prepareStatement(insert);

            ps.setString(1, user.getID().toString());
            ps.setString(2, user.getFirstName());
            ps.setString(3, user.getLastName());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getUsername());
            ps.setString(6, user.getPassword());
            ps.setString(7, user.getGender().name());

            if(user.getPreferredPronoun() == null)
                ps.setString(8, null);
            else
                ps.setString(8, user.getPreferredPronoun().name());

            ps.setString(9, user.getBirthday().toString());
            ps.setString(10, user.getDateOfAccountCreation().toString());

            Role role = user.getRole();
            ps.setString(11, role.getId().toString());

            int affectedRows = ps.executeUpdate();
            System.out.println("Affected Rows: " + affectedRows);

            ps.close();
        } catch(SQLException sqlEx) {
            System.out.println("Error in UserDAO.");
            System.out.println(sqlEx);
        } finally {
            jdbc.closeConnection();
        }
    }

    public boolean deleteUser(String userID) {
        jdbc = new JDBCUtil();
        jdbc.establishConnection();

        boolean isSuccessful = false;
        String delete = String.format("DELETE FROM users WHERE id = \"%s\"", userID);

        try {
            Statement st = jdbc.getConnection().createStatement();
            int affectedRows = st.executeUpdate(delete);
            System.out.println("Rows affected: " + affectedRows);

            isSuccessful = true;
        } catch (SQLException e) {
            System.out.println("UserDAO: Error in delete method");
            System.out.println(e);
        } finally {
            jdbc.closeConnection();
        }

        return isSuccessful;
    }

    private void updateField(String update) {
        try {
            jdbc = new JDBCUtil();
            jdbc.establishConnection();

            Statement st = jdbc.getConnection().createStatement();
            int affectedRows = st.executeUpdate(update);
            System.out.println("Rows affected: " + affectedRows);
        } catch (SQLException e) {
            System.out.println("UserDAO: Exception in updateName method.");
            System.out.println(e);
        } finally {
            jdbc.closeConnection();
        }
    }

    public void updateFirstName(String userID, String firstName) {
        String update = String.format("UPDATE users SET first_name = \"%s\" WHERE id = \"%s\"", firstName, userID);
        updateField(update);
    }

    public void updateLastName(String userID, String lastName) {
        String update = String.format("UPDATE users SET last_name = \"%s\" WHERE id = \"%s\"", lastName, userID);
        updateField(update);
    }

    public void updateName(String userID, String firstName, String lastName) {
        String update = String.format("UPDATE users SET first_name = \"%s\", last_name = \"%s\"" +
                " WHERE id = \"%s\"", firstName, lastName, userID);
        updateField(update);
    }

    public void updatePassword(String userID, String newPassword) {
        String update = String.format("UPDATE users SET password = \"%s\"" +
                " WHERE id = \"%s\"", newPassword, userID);
        updateField(update);
    }

    public void updateUsername(String userID, String username) {
        String update = String.format("UPDATE users SET username = \"%s\"" +
                " WHERE id = \"%s\"", username, userID);
        updateField(update);
    }

    public void updateEmail(String userID, String email) {
        String update = String.format("UPDATE users SET email = \"%s\"" +
                " WHERE id = \"%s\"", email, userID);
        updateField(update);
    }

    public void updateBirthday(String userID, String birthday) {
        String update = String.format("UPDATE users SET birthday = \"%s\"" +
                " WHERE id = \"%s\"", birthday, userID);
        updateField(update);
    }

    public void updateGender(String userID, Gender gender, PreferredPronoun pn) {
        String update = null;
        if(pn == null) {
            update = String.format("UPDATE users SET gender = \"%s\", preferredPronoun = NULL" +
                    " WHERE id = \"%s\"", gender.name(), userID);
        }
        else {
            update = String.format("UPDATE users SET gender = \"%s\", preferredPronoun = \"%s\"" +
                    " WHERE id = \"%s\"", gender.name(), pn.name(), userID);
        }

        updateField(update);
    }
}
