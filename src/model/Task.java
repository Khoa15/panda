package model;

import java.util.Date;

public class Task extends BaseClass {

    private int parentId;
    private Project project = new Project();
    private boolean isFullDay;
    private short typeLoop;
    private int doneAfterNDays;
    private boolean isDone;

    public Task() {
        super();
        parentId = -1;
        isFullDay = false;
        typeLoop = 0;
        doneAfterNDays = 0;
        isDone = false;
    }

    public Task(
            String description,
            Date dateStarted,
            Date dateEnded,
            int typeLoop,
            int nDays,
            boolean allDay,
            boolean done,
            String projectName,
            int idProject
    ) {
        this.description = description;
        this.setStartedAt(dateStarted);
        this.setEndedAt(dateEnded);
        this.typeLoop = (short) typeLoop;
        this.doneAfterNDays = nDays;
        this.isFullDay = allDay;
        this.isDone = done;
        this.project.id = idProject;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setProject(int id) {
        this.project = new Project();
        this.project.setId(id);
    }

    public boolean getIsFullDay() {
        return isFullDay;
    }

    public void setFullDay(boolean isFullDay) {
        this.isFullDay = isFullDay;
    }

    public short getTypeLoop() {
        return typeLoop;
    }

    public void setTypeLoop(short typeLoop) {
        this.typeLoop = typeLoop;
    }

    public int getDoneAfterNDays() {
        return doneAfterNDays;
    }

    public void setDoneAfterNDays(int doneAfterNDays) {
        this.doneAfterNDays = doneAfterNDays;
    }

    public boolean getIsDone() {
        return isDone;
    }

    public String getStrIsDone() {
        return (isDone == true) ? "Done" : "Nope";
    }

    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }
    
    public String getPairIdName(){
        return super.getPairIdName() + ", " + this.description;
    }
}
