package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static String getUsername() {
            return username;
    }
    public static void setUsername(String aUsername) {
        username = aUsername;
    }

    public static void setPassword(String aPassword) {
        password = aPassword;
    }
    private static String dbname = "PANDA";
    private static String srvname = "MUN\\SQLEXPRESS";
    private static String username = "sa";
    private static String password = "123";
    private static String strcon;
    private static String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static String url = "jdbc:sqlserver://"
            + srvname
            + ":1433;databaseName="
            + dbname
            + ";integratedSecurity = false;trustServerCertificate=True"
            + ";user="
            + username
            + ";password="
            + password;
    private static Connection con;

    public DBConnection() {
    }
    
    private static String getUrl(){
        url = "jdbc:sqlserver://"
            + srvname
            + ":1433;databaseName="
            + dbname
            + ";integratedSecurity = false;trustServerCertificate=True"
            + ";user="
            + username
            + ";password="
            + password;
        return url;
    }

    public static Connection openConnection() {
        try {
            try {
                Class.forName(driver);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            con = DriverManager.getConnection(getUrl());
            return con;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void closeConnection() {
        try {
            if (con.isClosed()) {
                return;
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
