package com.hargrove.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {
    private Connection connection;

    public void establishConnection() {
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
            connection = DriverManager.getConnection(url, userName, password);
        } catch(ClassNotFoundException e) {
            System.out.println(e);
        } catch(SQLException sqlEx) {
            System.out.println(sqlEx);
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch(SQLException exc) {
            System.out.println(exc);
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
