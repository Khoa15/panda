/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bll;

import dao.VocabDAO;
import java.util.ArrayList;
import model.Vocab;

/**
 *
 * @author Khoa
 */
public class VocabBLL {
    private ArrayList<Vocab> listVocabs = new ArrayList<Vocab>();
    public VocabBLL(){}
    
    public ArrayList<Vocab> load(){
        listVocabs.clear();
        try{
            listVocabs = VocabDAO.load();
        }catch(Exception e){
            
        }
        return listVocabs;
    }
}
