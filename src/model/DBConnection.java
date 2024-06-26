package model;

//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
import dao.DBConnectionDAO;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection extends Thread {

    public static String getSchema() {
        return "PANDA";
    }
    @Override
    public void run(){
        while (true) {
            try {
                if(con.isValid(10) == false){
                    System.out.println("Connection is dead");
                    System.exit(0);
                }
            } catch (SQLException ex) {
                Logger.getLogger(DBConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static String getUsername() {
        return username;
    }

    public static void setUsername(String aUsername) {
        
        username = normalize(aUsername).toUpperCase();
    }

    public static void setPassword(String aPassword) {
        password = aPassword;
    }
    
    public static Connection getConn(){
        if(con == null){
            return openConnection();
        }
        return con;
    }
    
    public static LocalDateTime getLastLogin(){
        return last_login;
    }

    public static Blob getAvatar() {
        return avatar;
    }

    public static Blob getRing_tone() {
        return ring_tone;
    }
    
    private final static String sysuser = "panda_register";
    private final static String syspass = "panda_register";
    private static String username = "";
    private static String password = "";
    private static Blob avatar = null;
    private static Blob ring_tone = null;
    private static LocalDateTime last_login = null;
    private static String database = "orcl";
    private static String url = "jdbc:oracle:thin:@localhost:1521/" + database + "?current_schema=PANDA";
    private static Connection con;

    public DBConnection() {
    }

    public static Connection openConnection(String username, String password) throws Exception {
        try {
            if(con != null &&  con.isClosed() == false) closeConnection();
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            con = DriverManager.getConnection(url, normalize(username), password);
            try{
                Timestamp last_login = (Timestamp)DBConnectionDAO.CallFunctionWithOutValues("get_last_login", Types.TIMESTAMP);
                DBConnection.last_login = last_login.toLocalDateTime();
                String sql = "SELECT avatar, ring_tone FROM PANDA.ACCOUNT WHERE USERNAME = '"+username.toUpperCase()+"'";
                ResultSet rs = DBConnectionDAO.ExecuteSelectQuery(sql);
                while(rs.next()){
                    avatar = rs.getBlob("avatar");
                    ring_tone = rs.getBlob("ring_tone");
                }
            }catch(Exception e){
                
            }
            if (con != null) {
                System.out.println("Connected");
            } else {
                System.out.println("Connected failed!");
            }
            return con;
        } catch (Exception e) {
            throw e;
        }
    }

    public static Connection openConnection() {
        try {
            if(con != null && con.isClosed() == false) closeConnection();
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            if(username.isEmpty()){
                con = DriverManager.getConnection(url, sysuser, syspass);
            }else{
                con = DriverManager.getConnection(url, username, password);
            }
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
    
    public boolean checkCurrentConnection(){
        try{
            con.isValid(10);
            
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }finally{
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
            username = sysuser;
            password = sysuser;
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
