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
    
    public static boolean add(Flashcard flashcard, int collection_id) throws Exception{
        try{
            Object values [] = new Object[] {
                flashcard.getFront(),
                flashcard.getBack(),
                collection_id,
            };
            return DBConnectionDAO.CallProcedureNoParameter("add_flashcard", values);
        }catch(Exception e){
            throw e;
        }finally{
            //DBConnection.closeConnection();
        }
    }
}
