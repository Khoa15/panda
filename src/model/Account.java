package model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;

public class Account {
    private byte[] avatar;

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public Account(byte[] avatar, String fullname, String username, String password) {
        this.avatar = avatar;
        this.fullname = fullname;
        this.username = username;
        this.password = password;
    }
    private String fullname;
    private String username;
    private String password;
    private static final String _file_session = "session_login.panda";
    public static String file_session()
    {
        return _file_session;
    }

    public Account() {
    }

    public Account(String username, String fullname, String password) {
        this.username = username;
        this.fullname = fullname;
        this.password = password;
    }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * @return the fullname
     */
    public String getFullname() {
        return fullname;
    }

    /**
     * @param fullname the fullname to set
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param email the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    public static void saveSessionDevice(String token) throws IOException{
        File file_session = new File(_file_session);
        boolean isCreated = file_session.createNewFile();
        FileOutputStream file_session_writer = new FileOutputStream(file_session);
        byte[] _token = token.getBytes();
        file_session_writer.write(_token);
        file_session_writer.flush();
        file_session_writer.close();
    }
    
    public static void deleteSessionDevice() throws IOException{
        File file_session = new File(_file_session);
        boolean isCreated = file_session.exists();
        if(isCreated){
            FileOutputStream file_session_writer = new FileOutputStream(file_session);
            file_session_writer.write(null);
            file_session_writer.close();
        }
    }
}
