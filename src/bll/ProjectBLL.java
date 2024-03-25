/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bll;

import dao.ProjectDAO;
import java.util.ArrayList;
import model.Project;

/**
 *
 * @author Khoa
 */
public class ProjectBLL {
    private static ArrayList<Project> listProjects = new ArrayList<>();

    public static ArrayList<Project> getListProjects() {
        return listProjects;
    }

    public static void delete(int selectedProjectId) throws Exception {
        try{
            ProjectDAO.delete(selectedProjectId);
            listProjects.remove(ProjectBLL.get(selectedProjectId));
        }catch(Exception e){
            throw e;
        }
    }
    
    public ProjectBLL(){}
    
    public static ArrayList<Project> load(){
        listProjects.clear();
        try{
            listProjects = ProjectDAO.load();
        }catch(Exception e){
            
        }
        return listProjects;
    }
    
    public static ArrayList<Project> loadFull(){
        listProjects.clear();
        try{
            listProjects = ProjectDAO.loadFull();
            
        }catch(Exception e){
            
        }
        return listProjects;
    }
    
    public static Project get(int id){
        for(Project p : listProjects){
            if(p.getId() == id){
                return p;
            }
        }
        return null;
    }
    
    public static Project get(String name){
        for(Project p : listProjects){
            if(p.getName().equals(name)){
                return p;
            }
        }
        return null;
    }
    
    public static Project find(String pairIdName){
        for(Project p : listProjects){
            if(p.getPairIdName().equals(pairIdName)){
                return p;
            }
        }
        return null;
    }
    
    public static String[] getList(){
        if(listProjects.size() == 0){
            listProjects = ProjectDAO.load();
        }
        String[] list = new String[listProjects.size()];
        for(int i = 0; i < listProjects.size(); i++){
            list[i] = listProjects.get(i).getPairIdName();
        }
        
        return list;
    }
    
    public static void add(Project project) throws Exception{
        try{
            ProjectDAO.add(project);
            ProjectBLL.load();
        }catch(Exception e){
            throw e;
        }
    }
    
    public static void update(Project project, String oldProjectName) throws Exception{
        try{
            ProjectDAO.update(project, oldProjectName);
            int index = findIndex(project.getId());
            if(index != -1){
                Project p = new Project(project);
                listProjects.remove(index);
                listProjects.add(p);
            }
        }catch(Exception e){
            throw e;
        }
    }
    
    
    
    
    //////////////////////
    private static int findIndex(int id){
        for(int i = 0; i < listProjects.size(); i++){
            if(listProjects.get(i).getId() == id){
                return i;
            }
        }
        return -1;
    }
}
