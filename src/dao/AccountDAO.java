/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.ResultSet;
import model.Account;
import model.DBConnection;

/**
 *
 * @author nguye
 */
public class AccountDAO {
    public AccountDAO(){}
    
    public static Account signIn(Account account){
        try{
            Object[] values = new Object[] {account.getEmail(), account.getPassword()};
            ResultSet rs = DBConnectionDAO.Load("SelectAccount", values);
            
            return setAccount(rs);
        }catch(Exception e){
            return null;
        }finally{
            DBConnection.closeConnection();
        }
    }
    
    public static boolean add(Account account){
        try{
            Object[] values = new Object[] {
                account.getEmail(),
                account.getFullname(),
                account.getPassword()
            };
            int res = DBConnectionDAO.Update("AddAccount", values);
            return res > 0;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    
    private static Account setAccount(ResultSet rs){
        try{
            if(rs.next()){
                String email = rs.getString("email");
                String fullname = rs.getString("fullname");

                return new Account(email, fullname, null);
            }else return null;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
