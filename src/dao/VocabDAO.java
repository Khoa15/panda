package dao;

import model.DBConnection;
import model.Vocab;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VocabDAO {

    private static ArrayList<Vocab> vocabs = new ArrayList<Vocab>();

    public VocabDAO() {
    }

    public static ArrayList<Vocab> load() {
        try {
            vocabs.clear();
            Object[] values = {
                DBConnection.getUsername()
            };
            ResultSet rs = DBConnectionDAO.Load("SelectVocabs");
            while (rs.next()) {
                vocabs.add(setVocab(rs));
            }
            return vocabs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            DBConnection.closeConnection();
        }
    }

    public static Vocab load(int id) {
        try {
            vocabs.clear();
            ResultSet rs = DBConnectionDAO.Load("SelectVocab", id);
            Vocab vc = setVocab(rs);
            return vc;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            DBConnection.closeConnection();
        }
    }

    public static ArrayList<Vocab> search(String word) {
        try {
            vocabs.clear();
            Object[] values = {
                word,
                DBConnection.getUsername()
            };
            ResultSet rs = DBConnectionDAO.Load("SelectVocabsByWord", values);
            while (rs.next()) {
                vocabs.add(setVocab(rs));
            }
            return vocabs;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            DBConnection.closeConnection();
        }
    }

    public static boolean add(Vocab v) {
        try {
            Object[] values = new Object[]{
                v.getWord(),
                v.getPartOfSpeech(),
                v.getMeaning(),
                v.getAudio(),
                v.getIpa()
            };
            return DBConnectionDAO.Update("AddVocab", values) > 0;
        } catch (Exception e) {
            return false;
        } finally {
            DBConnection.closeConnection();
        }
    }

    public static boolean update(Vocab v) {
        try {
            return DBConnectionDAO.Update("UpdateVocab", null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.closeConnection();
        }
    }

    public static int delete(int id) {
        try {

            return DBConnectionDAO.Update("DeleteVocab", null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            DBConnection.closeConnection();
        }
    }

    private static Vocab setVocab(ResultSet rs) {
        try {
            Vocab vc = new Vocab();
            vc.setId(rs.getInt("id"));
            vc.setWord(rs.getString("front"));
            vc.setMeaning(rs.getString("back"));

            return vc;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
