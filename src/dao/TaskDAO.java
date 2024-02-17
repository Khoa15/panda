package dao;

import java.sql.ResultSet;
import java.util.ArrayList;

import model.DBConnection;
import model.Task;

public class TaskDAO {
	private static ArrayList<Task> tasks = new ArrayList<Task>();
	
	public TaskDAO() {}
	
	public static ArrayList<Task> load(){
		try {
			tasks.clear();
			ResultSet rs = DBConnectionDAO.Load(null);
			while(rs.next()) {
				tasks.add(setTask(rs));
			}
			return tasks;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			DBConnection.closeConnection();
		}
	}
	
	public static ArrayList<Task> load(int id){
		try {
			tasks.clear();
			ResultSet rs = DBConnectionDAO.Load(null, id);
			while(rs.next()) {
				tasks.add(setTask(rs));
			}
			return tasks;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}finally {
			DBConnection.closeConnection();
		}
	}
	
	public static int update(Task project) {
		try {
			return DBConnectionDAO.Update("UpdateTask", null);
		}catch(Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			DBConnection.closeConnection();
		}
	}
	
	public static int delete(int id) {
		try {

			return DBConnectionDAO.Update("DeleteTask", null);
		}catch(Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			DBConnection.closeConnection();
		}
	}
	
	
	private static Task setTask(ResultSet rs) {
		try {
			Task project = new Task();
			
			return project;
		}catch(Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
