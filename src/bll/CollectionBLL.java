/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bll;

import dao.CollectionDAO;
import java.util.ArrayList;
import model.Collection;

/**
 *
 * @author Khoa
 */
public class CollectionBLL {
    private static ArrayList<Collection> listCollections = new ArrayList<>();

    public static ArrayList<Collection> getListCollections() {
        return listCollections;
    }
    public CollectionBLL(){}
    
    public static ArrayList<Collection> load(){
        listCollections.clear();
        try{
            listCollections = CollectionDAO.load();
        }catch(Exception e){
            
        }
        return listCollections;
    }
    
    public static int findIndex(String collectionName){
        for(int i = 0; i < listCollections.size(); i++){
            if(listCollections.get(i).getName().equals(collectionName)) return i;
        }
        return -1;
    }
    
    public static void update(Collection collection){
        try{
            
        }catch(Exception e){
            
        }        
    }
    
    public static void insert(Collection collection){
        try{
            
        }catch(Exception e){
            
        }
    }
    
    public static void delete(int id){
        try{
            
        }catch(Exception e){
            
        }
    }
}
