package dao;

import java.sql.Timestamp;
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
            DBConnectionDAO.CallProcedureNoParameter("Insert_Inbox", values);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            //DBConnection.closeConnection();
        }
    }

    public static void add(Task task) throws Exception {
        try{
            Object[] values = new Object[]{
                task.getProject().getId(),
                task.getDescription(),
                task.getIsDone(),
                0,// type_date
                task.getIsFullDay(),
                task.getTypeLoop(),
                task.getDoneAfterNDays(),
                task.getPriority(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getStartedAt(),
                task.getEndedAt()
            };
            DBConnectionDAO.CallProcedureNoParameterOut("add_task", values);
        }catch(Exception e){
            throw e;
        }
    }

    public static Task get(String taskDescription) throws Exception {
        try{
            ResultSet rs = DBConnectionDAO.CallFunction("select_task_by_description", taskDescription);
            if(rs == null){
                throw new IllegalStateException("Task not found!");
            }
            return setTask(rs);
        }catch(Exception e){
            throw e;
        }
    }

    public TaskDAO() {
    }

    public static ArrayList<Task> load() {
        try {
            tasks.clear();
            ResultSet rs = DBConnectionDAO.CallFunction("select_tasks");
            if(rs == null) return tasks;
            while (rs.next()) {
                Task t = setTask(rs);
                tasks.add(t);
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
            if(rs == null) return tasks;
            while (rs.next()) {
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

    public static void update(Task task) throws Exception {
        try {
            Object[] values = new Object[]{
                task.getId(),
                task.getProject().getId(),
                task.getDescription(),
                task.getIsDone(),
                0,// type_date
                task.getIsFullDay(),
                task.getTypeLoop(),
                task.getDoneAfterNDays(),
                task.getPriority(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getStartedAt(),
                task.getEndedAt()
            };
            DBConnectionDAO.CallProcedureNoParameterOut("Update_Task", values);
        } catch (Exception e) {
            throw e;
        }
    }

    public static int delete(int id) throws Exception {
        try {
            Object[] values = new Object[]{
                id
            };
            return DBConnectionDAO.Update("Delete_Task", values);
        } catch (Exception e) {
            throw e;
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
            task.setId(rs.getInt("id"));
            task.setProject(rs.getInt("pid"));
            task.setDescription(rs.getNString("description"));
            task.setDone(rs.getBoolean("done"));
            task.setFullDay(rs.getBoolean("is_full_day"));
            task.setTypeLoop((short)rs.getInt("type_loop"));
            task.setDoneAfterNDays(rs.getInt("done_after_n_days"));
            task.setPriority(rs.getByte("priority"));
            
            task.setCreatedAt(rs.getTimestamp("created_at"));
            task.setUpdatedAt(rs.getTimestamp("updated_at"));
            task.setStartedAt(rs.getTimestamp("started_at"));
            task.setEndedAt(rs.getTimestamp("ended_at"));
            return task;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
