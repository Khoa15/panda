package dao;

import java.sql.ResultSet;
import java.util.ArrayList;

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
            
            return DBConnectionDAO.Update("InsertInbox", values) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            DBConnection.closeConnection();
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
            DBConnection.closeConnection();
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
            DBConnection.closeConnection();
        }
    }

    public static ArrayList<Task> loadTaskToday() {
        try {
            tasks.clear();
            ResultSet rs = DBConnectionDAO.Load("SelectTaskToday");
            while (rs.next()) {
                if(rs.getRow() == 0) continue;
                tasks.add(setTask(rs));
            }
            return tasks;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            DBConnection.closeConnection();
        }
    }

    public static ArrayList<Task> loadInboxes() {
        try {
            tasks.clear();
            ResultSet rs = DBConnectionDAO.Load("SelectInboxes");
            while (rs.next()) {
                if(rs.getRow() == 0) continue;
                tasks.add(setTask(rs));
            }
            return tasks;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            DBConnection.closeConnection();
        }
    }

    public static int update(Task project) {
        try {
            return DBConnectionDAO.Update("UpdateTask", null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            DBConnection.closeConnection();
        }
    }

    public static int delete(int id) {
        try {

            return DBConnectionDAO.Update("DeleteTask", null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            DBConnection.closeConnection();
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
