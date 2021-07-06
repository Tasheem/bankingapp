package com.hargrove.dao;

import com.hargrove.models.Role;

import java.sql.*;
import java.util.UUID;

public class RoleDAO {
    Connection con;

    private void establishConnection() {
        /*
         * Server timezone has to be specified in the connection string
         * or a SQLException will be thrown.
         *
         * Source: https://stackoverflow.com/questions/26515700/mysql-jdbc-driver-5-1-33-time-zone-issue
         */
        String url = "jdbc:mysql://localhost:3306/"
                + "banking?useUnicode=true&useJDBCCompliantTimezoneShift="
                + "true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String userName = "root";
        String password = "colts1810";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, userName, password);
        } catch(ClassNotFoundException e) {
            System.out.println(e);
        } catch(SQLException sqlEx) {
            System.out.println(sqlEx);
        }
    }

    private void closeConnection() {
        try {
            con.close();
        } catch(SQLException exc) {
            System.out.println(exc);
        }
    }

    public Role queryRole(String roleName) {
        establishConnection();

        String query = String.format("SELECT * FROM roles WHERE name = \"%s\";", roleName);
        Role role = null;

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            rs.next();

            UUID id = UUID.fromString(rs.getString(1));
            role = new Role(id, roleName);

        } catch (SQLException sqlException) {
            System.out.println(sqlException);
        }

        closeConnection();
        return role;
    }
}
