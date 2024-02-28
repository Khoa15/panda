/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import model.DBConnection;
import model.Sentence;
import model.Vocab;

/**
 *
 * @author Khoa
 */
public class SentenceDAO {
    
    public SentenceDAO(){}
    
    public static boolean add(Vocab v){
        try{
            int r = 0;
            for(Sentence s : v.getSentence()){
                Object[] values = new Object[]{
                    s.getOrigin(),
                    s.getTranslated(),
                    0, // Type
                    v.getWord(),
                    v.getPartOfSpeech()
                };
                r += DBConnectionDAO.Update("AddSentence", values);
            }
            return r > 0;
        }catch(Exception e){
            return false;
        }finally{
            //DBConnection.closeConnection();
        }
    }
}
