package dao;

import java.util.ArrayList;
import java.sql.ResultSet;

import model.Card;
import model.DBConnection;
import model.Flashcard;
import model.Vocab;

public class CardDAO {

    private static ArrayList<Card> cards = new ArrayList<Card>();

    public CardDAO() {
    }

    public static ArrayList<Card> load(int collectionId) {
        try {
            cards.clear();
            Object[] values = new Object[]{collectionId};
            ResultSet rs = DBConnectionDAO.Load("SelectCards", values);
            while (rs.next()) {
                cards.add(setCard(rs));
            }
            return cards;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            //DBConnection.closeConnection();
        }
    }
    
    public static int[] loadAnalysisMemory(){
        try{
            int[] memories = new int[10];
            ResultSet rs = DBConnectionDAO.Load("SelectCardsAnalysisMemory");
            while(rs.next()){
                int m = rs.getInt("memory");
                int c = rs.getInt("count");
                memories[m] = c;
            }
            return memories;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }finally{
            //DBConnection.closeConnection();
        }
    }

//    public static ArrayList<Card> load(Object id) {
//        try {
//            Object[] values = new Object[]{id};
//            ResultSet rs = DBConnectionDAO.Load("SelectCards", values);
//            return setCard(rs);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        } finally {
//            DBConnection.closeConnection();
//        }
//    }

    public static int addCard(Card c) {
        try {
            Object[] values = new Object[]{};
            return DBConnectionDAO.Update("AddCard", values);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            //DBConnection.closeConnection();
        }
    }

    public static int updateCard(Card c) {
        try {
            Object[] values = new Object[]{
                c.getId(),};
            return DBConnectionDAO.Update("UpdateCard", values);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            //DBConnection.closeConnection();
        }
    }

    public static int updateMemoryCard(Card c) {
        try {
            Object[] values = new Object[]{
                c.getId(),
                c.getMemory()
            };
            return DBConnectionDAO.Update("UpdateMemoryCard", values);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            //DBConnection.closeConnection();
        }
    }

    public static int deleteCard(int id) {
        try {
            Object[] values = new Object[]{id};
            return DBConnectionDAO.Update("DeleteCard", values);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            //DBConnection.closeConnection();
        }
    }

    private static Card setCard(ResultSet rs) {
        try {
            int card_id = rs.getInt("card_id");
            int collection_id = rs.getInt("collection_id");
            String front = rs.getNString("front");
            String back = rs.getNString("back");
            String word = rs.getNString("word");
            String meaning = rs.getNString("meaning");
            String pos = rs.getNString("pos");
            Card c;
            if(word == null){
                Flashcard f = new Flashcard();
                f.setFront(front);
                f.setBack(back);
                c = f;
                c.setIsFlashcard(true);
            }else{
                Vocab v = new Vocab();
                v.setWord(word);
                v.setMeaning(meaning);
                v.setPartOfSpeech(pos);
                c = v;
            }
            
            return c;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
