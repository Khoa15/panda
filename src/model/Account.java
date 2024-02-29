package model;

import java.time.LocalDate;

public class Account {

    private String fullname;
    private String username;
    private String password;

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
}
