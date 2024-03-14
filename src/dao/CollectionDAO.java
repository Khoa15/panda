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
            ResultSet rs = DBConnectionDAO.CallFunction("Select_Collections");
            while(rs.next()){
                collections.add(setCollection(rs));
            }
            return collections;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    public static ArrayList<Collection> loadWithAnalysis(){
        try{
            collections.clear();
            ResultSet rs = DBConnectionDAO.CallFunction("Select_Collections_Analysis");
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
                c.getName()
            };
            
            return DBConnectionDAO.Update("Add_Collection", values) > 0;
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
            int res = DBConnectionDAO.Update("Delete_Collection", values);
            return res > 0;
        }catch(Exception e){
            return false;
        }finally{
            //DBConnection.closeConnection();
        }
    }
    
    private static Collection setCollection(ResultSet rs){
        try{
            Collection c = new Collection();
            c.setId(rs.getInt("id"));
            c.setName(rs.getNString("name"));
            int count = rs.getMetaData().getColumnCount();
            
            if(count > 3){
                int total_card = rs.getInt("total_card");
                int n_learn = rs.getInt("n_learn");
                int n_missed = rs.getInt("n_missed");
                
                c.setTotalCard(total_card);
                if(n_missed != 0)
                    c.setPercentTrue((float) n_learn / n_missed);
                int lv5 = rs.getInt("memory");
                if(lv5 != 0){
                    c.setPercentLevel5((float) lv5 / total_card);
                }
            }
            return c;
        }catch(Exception e){
            return null;
        }
    }
}
