package dao;

import model.DBConnection;
import model.Vocab;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import model.Sentence;

public class VocabDAO {

    private static ArrayList<Vocab> vocabs = new ArrayList<Vocab>();

    public VocabDAO() {
    }

    public static ArrayList<Vocab> load() {
        vocabs.clear();
        try {
            Object[] values = {
                DBConnection.getUsername()
            };
            ResultSet rs = DBConnectionDAO.CallFunction("Select_Vocab");
            while (rs.next()) {
                vocabs.add(setVocab(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //DBConnection.closeConnection();
        }
        return vocabs;
    }

    public static DefaultTableModel getDataTable(){
        try{
            ResultSet resultSet = DBConnectionDAO.CallFunction("select_vocab");
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            DefaultTableModel tableModel = new DefaultTableModel(0, columnCount);

            Vector<String> columnNames = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }
            columnNames.add("Sửa");
            columnNames.add("Xóa");
            tableModel.setColumnIdentifiers(columnNames);

            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                int entityId = resultSet.getInt(1);
                JButton editButton = new JButton("Sửa");
                JButton deleteButton = new JButton("Xóa");
                //editButton.addActionListener(e -> editEntity(entityId));
                //deleteButton.addActionListener(e -> deleteEntity(entityId));
                rowData[columnCount] = editButton;
                rowData[columnCount + 1] = deleteButton;
                
                tableModel.addRow(rowData);
            }

            return tableModel;
        }catch(Exception e){
            return null;
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
            //DBConnection.closeConnection();
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
            //DBConnection.closeConnection();
        }
    }

    public static boolean add(Vocab v) throws Exception {
        try {
            Object[] values = new Object[]{
                v.getWord(),
                v.getPartOfSpeech(),
                v.getMeaning(),
                v.getAudio(),
                v.getIpa()
            };
            DBConnectionDAO.CallProcedureNoParameterOut("add_vocab", values);
            return  true;
        } catch (Exception e) {
            throw e;
        } finally {
            //DBConnection.closeConnection();
        }
    }

    public static boolean update(Vocab v) {
        try {
            return DBConnectionDAO.Update("UpdateVocab", null) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            //DBConnection.closeConnection();
        }
    }

    public static int delete(int id) {
        try {

            return DBConnectionDAO.Update("DeleteVocab", null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            //DBConnection.closeConnection();
        }
    }

    private static Vocab setVocab(ResultSet rs) {
        try {
            Vocab vc = new Vocab();
            //vc.setId(rs.getInt("id"));
            vc.setWord(rs.getString("word"));
            vc.setMeaning(rs.getString("meaning"));
            vc.setPartOfSpeech(rs.getString("pos"));
            Sentence sentence = new Sentence();
            sentence.setOrigin(rs.getString("origin"));
            vc.setSentence(sentence);
            return vc;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
