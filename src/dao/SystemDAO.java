/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import model.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import oracle.jdbc.OracleTypes;

/**
 *
 * @author Khoa
 */
public class SystemDAO {

    public SystemDAO() {
    }

    public static DefaultTableModel LoadSGA() {
        try {
            return setDefaultDataTableModel("GetSgaInfo");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DefaultTableModel LoadPGA(){
        try {
            return setDefaultDataTableModel("GetPgaStat");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    
    }
    
    public static DefaultTableModel LoadControlFile(){
        try {
            return setDefaultDataTableModel("GetControlfileinfo");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static DefaultTableModel LoadDatabaseInfo(){
        try {
            return setDefaultDataTableModel("GetDatabaseInfo");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
        
    public static DefaultTableModel LoadDataFileInfo(){
        try {
            return setDefaultDataTableModel("GetDatafileinfo");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
           
    public static DefaultTableModel LoadInstanceInfo(){
        try {
            return setDefaultDataTableModel("GetInstanceInfo");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
               
    public static DefaultTableModel LoadProcessInfo(){
        try {
            return setDefaultDataTableModel("GetProcessInfo");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
          
    public static DefaultTableModel LoadSpFileInfo(){
        try {
            return setDefaultDataTableModel("GetSpfileinfo");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static DefaultTableModel LoadSession(){
        try {
            return setDefaultDataTableModel("SelectSession");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static DefaultTableModel LoadTableSpace(){
        try {
            return setDefaultDataTableModel("GetTableSpaces");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static DefaultTableModel loadInfoSystem(String type) {
        try {
            String table;
            switch (type) {
                case "PGA":
                    table = "v$pgastat";
                    break;
                case "PROCESS":
                    table = "v$process";
                    break;
                case "INSTANCE":
                    table = "v$instance";
                    break;
                case "DATABASE":
                    table = "v$database";
                    break;
                case "DATAFILE":
                    table = "v$datafile";
                    break;
                case "CONTROL FILES":
                    table = "v$controlfile";
                    break;
                case "SPFILE":
                    table = "v$spparameter";
                    break;
                case "SESSION":
                    table = "v$session where type!='BACKGROUND'";
                    break;
                case "TABLESPACE":
                    table = "dba_tablespaces";
                    break;
                default:
                    table = "v$sgainfo";
                    break;
            }
            ResultSet resultSet = DBConnectionDAO.ExecuteSelectQuery("SELECT * FROM " + table);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            DefaultTableModel tableModel = new DefaultTableModel(0, columnCount);

            Vector<String> columnNames = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }
            tableModel.setColumnIdentifiers(columnNames);

            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                tableModel.addRow(rowData);
            }

            return tableModel;
//            ResultSet resultSet = DBConnectionDAO.ExecuteSelectQuery("SELECT * FROM "+table);
//            ResultSetMetaData metaData = resultSet.getMetaData();
//            int columnCount = metaData.getColumnCount();
//            DefaultTableModel tableModel = new DefaultTableModel(new Object[columnCount], 0);
//
//            while (resultSet.next()) {
//                Object[] rowData = new Object[columnCount];
//                for (int i = 1; i <= columnCount; i++) {
//                    rowData[i - 1] = resultSet.getObject(i);
//                }
//                tableModel.addRow(rowData);
//            }
//
//            return tableModel;
            //jtable.setModel(tableModel);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static boolean killSession(Object sid, Object serial){
        try{
            Connection connection = DBConnection.getConn();// Establish database connection

            String sql = "ALTER SYSTEM KILL SESSION '" + sid + ", " + serial + "' IMMEDIATE";
//            PreparedStatement statement = connection.prepareStatement(sql);
//            statement.setString(1, sid.toString());
//            statement.setString(2, serial.toString());

            return DBConnectionDAO.ExecuteScalarQuery(sql);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static DefaultTableModel LoadProcessWithSession(String sid){
        try{
            Object[] values = {sid};
            ResultSet resultSet = DBConnectionDAO.CallFunction("get_process_with_session", values, OracleTypes.CURSOR);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            DefaultTableModel tableModel = new DefaultTableModel(new Object[columnCount], 0);

            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                tableModel.addRow(rowData);
            }

            return tableModel;
        }catch(Exception e){
            return null;
        }
    }
    
    private static DefaultTableModel setDefaultDataTableModel(String name){
        try{
            ResultSet resultSet = DBConnectionDAO.Load(name);
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            DefaultTableModel tableModel = new DefaultTableModel(0, columnCount);

            Vector<String> columnNames = new Vector<>();
            for (int i = 1; i <= columnCount; i++) {
                columnNames.add(metaData.getColumnName(i));
            }
            tableModel.setColumnIdentifiers(columnNames);

            while (resultSet.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resultSet.getObject(i);
                }
                tableModel.addRow(rowData);
            }

            return tableModel;
        }catch(Exception e){
            return null;
        }
    }
}
