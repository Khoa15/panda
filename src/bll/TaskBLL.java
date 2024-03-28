/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bll;

import dao.TaskDAO;
import java.time.LocalDateTime;
import java.util.ArrayList;
import model.Task;

/**
 *
 * @author Khoa
 */
public class TaskBLL {

    public static void add(Task task) throws Exception {
        try{
            TaskDAO.add(task);
        }catch(Exception e){
            throw e;
        }
    }

    public static void update(Task task) throws Exception {
        try{
            TaskDAO.update(task);
        }catch(Exception e){
            throw e;
        }
    }

    public static void delete(int id) throws Exception {
        try{
            TaskDAO.delete(id);
        }catch(Exception e){
            throw e;
        }
    }
    private ArrayList<Task> listTasks = new ArrayList<Task>();
    public TaskBLL(){}
    
    public ArrayList<Task> load(){
        listTasks.clear();
        try{
            listTasks = TaskDAO.load();
        }catch(Exception e){
            
        }
        return listTasks;
    }
    
    public static Task get(String taskDescription){
        try{
            return TaskDAO.get(taskDescription);
        }catch(Exception e){
            
        }
        return null;
    }
    
    public ArrayList<Task> getTaskToday(){
        ArrayList<Task> filtered = new ArrayList<>();
        return filtered;
    }
}
