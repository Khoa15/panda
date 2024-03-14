package dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;

import model.DBConnection;
import model.Task;

public class TaskDAO {

    private static ArrayList<Task> tasks = new ArrayList<Task>();

    public static boolean insertInbox(String inbox) {
        try {
            if(inbox.isEmpty() || inbox.isBlank()) return false;
            Object[] values ={
                inbox
            };
            
            return DBConnectionDAO.Update("Insert_Inbox", values) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            //DBConnection.closeConnection();
        }
    }

    public TaskDAO() {
    }

    public static ArrayList<Task> load() {
        try {
            tasks.clear();
            ResultSet rs = DBConnectionDAO.Load(null);
            while (rs.next()) {
                if(rs.getRow() == 0) continue;
                tasks.add(setTask(rs));
            }
            return tasks;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            //DBConnection.closeConnection();
        }
    }

    public static ArrayList<Task> load(int id) {
        try {
            tasks.clear();
            ResultSet rs = DBConnectionDAO.Load(null, id);
            while (rs.next()) {
                if(rs.getRow() == 0) continue;
                tasks.add(setTask(rs));
            }
            return tasks;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            //DBConnection.closeConnection();
        }
    }

    public static ArrayList<Task> loadTasksToday() {
        try {
            tasks.clear();
            ResultSet rs = DBConnectionDAO.CallFunction("Select_Tasks_Today");
            while (rs.next()) {
                if(rs.getRow() == 0) continue;
                tasks.add(setTask(rs));
            }
            return tasks;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            //DBConnection.closeConnection();
        }
    }
    
    public static ArrayList<Task> loadTasksToday(int pid) {
        try {
            tasks.clear();
            Object[] values ={
                pid
            };
            ResultSet rs = DBConnectionDAO.Load("Select_Tasks_Today_By_Project_Id", values);
            while (rs.next()) {
                if(rs.getRow() == 0) continue;
                tasks.add(setTask(rs));
            }
            return tasks;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            //DBConnection.closeConnection();
        }
    }

    public static ArrayList<Task> loadInboxes() {
        try {
            tasks.clear();
            ResultSet rs = DBConnectionDAO.CallFunction("Select_Inboxes");
            while (rs.next()) {
                if(rs.getRow() == 0) continue;
                tasks.add(setTask(rs));
            }
            return tasks;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            //DBConnection.closeConnection();
        }
    }

    public static int update(Task project) {
        try {
            return DBConnectionDAO.Update("Update_Task", null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            //DBConnection.closeConnection();
        }
    }

    public static int delete(int id) {
        try {

            return DBConnectionDAO.Update("Delete_Task", null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            //DBConnection.closeConnection();
        }
    }
    
    public static DefaultTableModel getDataTable(){
        try{
            ResultSet resultSet = DBConnectionDAO.CallFunction("select_projects");
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

    private static Task setTask(ResultSet rs) {
        try {
            Task task = new Task();
            task.setDescription(rs.getNString("description"));
            return task;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
