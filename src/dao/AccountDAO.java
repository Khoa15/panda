/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import model.Account;
import model.DBConnection;

/**
 *
 * @author nguye
 */
public class AccountDAO {

    public AccountDAO() {
    }

    public static boolean signIn(Account account) {
        try {
            
            Object[] values = new Object[]{account.getUsername(), account.getPassword()};

            if (DBConnection.openConnection(account.getUsername(), account.getPassword()) != null) {
                DBConnection.setUsername(account.getUsername());
                DBConnection.setPassword(account.getPassword());
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        } finally {
            //DBConnection.closeConnection();
        }
    }

    public static boolean add(Account account) {
        try {
            Object[] values = new Object[]{
                account.getAvatar(),
                account.getUsername(),
                account.getFullname(),
                account.getPassword()
            };
            DBConnection.openConnection();
            int res = DBConnectionDAO.Update("Add_Account", values);
            return res > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally{
            DBConnection.closeConnection();
        }
    }
    
    private static boolean signOut(){
        try{
            Object[] values = {
                DBConnection.getUsername()
            };
            
            return DBConnectionDAO.Update("SignOut", values) > 0;
        }catch(Exception e){
            return false;
        }
    }

    private static Account setAccount(ResultSet rs) {
        try {
            if (rs.next()) {
                String username = rs.getString("username");
                String fullname = rs.getString("fullname");

                return new Account(username, fullname, null);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
