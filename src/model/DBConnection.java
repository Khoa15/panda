package model;

//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
import java.sql.*;

public class DBConnection {

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String aUsername) {
        
        username = normalize(aUsername);
    }

    public static void setPassword(String aPassword) {
        password = aPassword;
    }
    private static String username = "panda";//"sa";
    private static String password = "panda";//"123";
    private static String database = "orclpdb";
    private static String url = "jdbc:oracle:thin:@localhost:1521/" + database;
    private static Connection con;

    public DBConnection() {
    }

    public static Connection openConnection(String username, String password) {
        try {
            if(con != null &&  con.isClosed() == false) closeConnection();
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            con = DriverManager.getConnection(url, normalize(username), password);
            if (con != null) {
                System.out.println("Connected");
            } else {
                System.out.println("Connected failed!");
            }
            return con;
        } catch (Exception e) {
            return null;
        }
    }

    public static Connection openConnection() {
        try {
            if(con != null && con.isClosed() == false) closeConnection();
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            con = DriverManager.getConnection(url, username, password);
            if (con != null) {
                System.out.println("Connected");
            } else {
                System.out.println("Connected failed!");
            }

            return con;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int closeConnection() {
        try {
            if (con == null) {
                return 0;
            }
            if (con.isClosed()) {
                return 0;
            }
            con.close();
            return 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static String normalize(String username) {

        username = username.replaceAll("\\.", "");
        username = username.replaceAll("@", "");
        return username;
    }
}
