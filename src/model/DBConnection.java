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
    private static String dbname = "PANDA";
    private static String srvname = "MUN\\SQLEXPRESS";
    private static String username = "panda";//"sa";
    private static String password = "panda";//"123";
    private static String strcon;
    private static String driver = "oracle.jdbc.driver.OracleDriver";//"com.microsoft.sqlserver.jdbc.SQLServerDriver";
    private static String url = "jdbc:oracle:thin:panda/panda@localhost:1521:orcl";//"jdbc:sqlserver://"
    //jdbc:oracle:thin:HR/<password>@localhost:5221:orcl

//            + srvname
//            + ":1433;databaseName="
//            + dbname
//            + ";integratedSecurity = false;trustServerCertificate=True"
//            + ";user="
//            + username
//            + ";password="
//            + password;
    private static Connection con;

    public DBConnection() {
    }

    private static String getUrl() {
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
//            OracleDataSource ods = new OracleDataSource();
//            ods.setDriverType("oci");
//            ods.setServerName("localhost");
//            ods.setNetworkProtocol("tcp");
//            ods.setDatabaseName("panda");
//            ods.setPortNumber(5221);
//            ods.setUser("panda");
//            ods.setPassword("panda");
//            Connection conn = ods.getConnection();
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            con = DriverManager.getConnection(url, username, password);
            if (con != null) {
                System.out.println("Connected");
            } else {
                System.out.println("Connected failed!");
            }
            String q = "SELECT * FROM accounts";

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
