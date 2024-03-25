/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import model.DBConnection;
import model.Sentence;
import model.Vocab;

/**
 *
 * @author Khoa
 */
public class SentenceDAO {
    
    public SentenceDAO(){}
    
    public static boolean add(ArrayList<Sentence> sentences, Vocab v) throws Exception {
        try{
            int r = 0;
            for(Sentence s : sentences){
                Object[] values = new Object[]{
                    s.getOrigin(),
                    s.getTranslated(),
                    0, // Type
                    v.getWord(),
                    v.getPartOfSpeech()
                };
                r += DBConnectionDAO.Update("Add_Sentence", values);
            }
            return r > 0;
        }catch(Exception e){
            throw e;
        }finally{
            //DBConnection.closeConnection();
        }
    }
    
    public static DefaultTableModel getDataTable(){
        try{
            ResultSet resultSet = DBConnectionDAO.CallFunction("select_sentences");
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
                Object[] rowData = new Object[columnCount + 2];
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
}
