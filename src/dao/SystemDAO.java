/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.sql.ResultSet;

/**
 *
 * @author Khoa
 */
public class SystemDAO {
    public SystemDAO(){}
   
    public HashMap<String, String> LoadSGA(){
        try{
            ResultSet rs = DBConnectionDAO.ExecuteSelectQuery("SELECT * FROM v$sgainfo;");
            HashMap<String, String> result = new HashMap<String, String>();
            while(rs.next()){
                result.put(rs.getNString(1), rs.getNString(2));
            }
            return result;
        }catch(Exception e){
            return null;
        }
    }
}
