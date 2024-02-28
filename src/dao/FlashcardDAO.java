/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.DBConnection;
import model.Flashcard;

/**
 *
 * @author Khoa
 */
public class FlashcardDAO {
    
    public FlashcardDAO(){}
    
    public static boolean add(Flashcard flashcard, int collection_id){
        try{
            Object values [] = new Object[] {
                flashcard.getFront(),
                flashcard.getBack(),
                collection_id,
            };
            return DBConnectionDAO.Update("AddFlashcard", values) > 0;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }finally{
            //DBConnection.closeConnection();
        }
    }
}
