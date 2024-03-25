package model;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class Project extends BaseClass {

    private String name;
    private ArrayList<Task> tasks = new ArrayList<>();

    public Project() {
        super();
    }
    
    public Project(String name, String description, byte priority, LocalDateTime startedAt, LocalDateTime endedAt){
        super();
        this.name = name;
        this.description = description;
        this.priority = priority;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    public Project(Project project) {
        this.id = project.id;
        this.name = project.name;
        this.description = project.description;
        this.priority = project.priority;
        this.startedAt =  project.startedAt;
        this.endedAt =  project.endedAt;
        this.createdAt = project.createdAt;
        this.updatedAt = project.updatedAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }
    
    public void addTask(Task task){
        tasks.add(task);
    }
    
    public String getPairIdName(){
        return super.getPairIdName() + ", " + this.name;
    }
}