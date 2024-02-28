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
        username = aUsername;
    }

    public static void setPassword(String aPassword) {
        password = aPassword;
    }
    private static String username = "panda";//"sa";
    private static String password = "panda";//"123";
    private static String url = "jdbc:oracle:thin:@localhost:1521/orclpdb";
    private static Connection con;

    public DBConnection() {
    }

    public static Connection openConnection() {
        try {
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

    public static void closeConnection() {
        try {
            if (con == null) {
                return;
            }
            if (con.isClosed()) {
                return;
            }
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
