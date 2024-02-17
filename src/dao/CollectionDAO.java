/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.ArrayList;
import model.Collection;
import java.sql.ResultSet;
import model.DBConnection;
/**
 *
 * @author Khoa
 */
public class CollectionDAO {
    private static ArrayList<Collection> collections = new ArrayList<Collection>();
    public CollectionDAO(){}
    public static ArrayList<Collection> load(){
        try{
            collections.clear();
            Object[] values = new Object[]{
                DBConnection.getUsername()
            };
            ResultSet rs = DBConnectionDAO.Load("SelectCollections", values);
            while(rs.next()){
                collections.add(setCollection(rs));
            }
            return collections;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    public static boolean add(Collection c){
        try{
            Object[] values = new Object[] {
                DBConnection.getUsername(),
                c.getName()
            };
            
            return DBConnectionDAO.Update("AddCollection", values) > 0;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }
    
    public static boolean delete(Collection c){
        try{
            Object[] values = {
                c.getName()
            };
            return DBConnectionDAO.Update("DeleteCollection", values) > 0;
        }catch(Exception e){
            return false;
        }finally{
            DBConnection.closeConnection();
        }
    }
    
    private static Collection setCollection(ResultSet rs){
        try{
            Collection c = new Collection();
            c.setId(rs.getInt("id"));
            c.setName(rs.getNString("name"));
            return c;
        }catch(Exception e){
            return null;
        }
    }
}
