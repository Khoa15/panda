package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.Date;
public class BaseClass {

    protected int id;
    protected String description;
    protected byte priority;
    protected LocalDate createdAt;
    protected LocalDate updatedAt;
    protected LocalDateTime startedAt;
    protected LocalDateTime endedAt;

    protected String getPairIdName(){
        return String.valueOf(this.id);
    }
    
    public static int getIdFromPair(String pairIdName){
        try{
            if(pairIdName == null || pairIdName.length() == 0) return -1;
            return Integer.parseInt(pairIdName.split(",")[0]);
        }catch(Exception e){
            return -1;
        }
    }

    public BaseClass() {
        this.id = -1;
        this.priority = 0;
        this.description = null;
        this.priority = 0;
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
        this.startedAt = null;
        this.endedAt = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte getPriority() {
        return priority;
    }

    public void setPriority(byte priority) {
        this.priority = priority;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        if(createdAt != null){
            this.createdAt = createdAt.toLocalDateTime().toLocalDate();
        }else{
            this.createdAt = null;
        }
    }
    
    public void setCreatedAt(Date createdAt) {
        if(createdAt != null){
            this.createdAt = createdAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }else{
            this.createdAt = null;
        }
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        if(updatedAt != null){
            this.updatedAt = updatedAt.toLocalDateTime().toLocalDate();
        }else{
            this.updatedAt = null;
        }
    }
    
    public void setUpdatedAt(Date updatedAt) {
        if(updatedAt != null){
            this.updatedAt = updatedAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }else{
            this.updatedAt = null;
        }
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }
    
    public Date getDateStartedAt(){
        if(this.startedAt == null) return null;
        return Date.from(this.startedAt.atZone(ZoneId.systemDefault()).toInstant());
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public void setStartedAt(Timestamp startedAt) {
        if(startedAt != null){
            this.startedAt = startedAt.toLocalDateTime();
        }else{
            this.startedAt = null;
        }
    }

    public void setStartedAt(Date startedAt) {
        if(startedAt != null){
            this.startedAt = startedAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }else{
            this.startedAt = null;
        }
    }

    public LocalDateTime getEndedAt() {
        return endedAt;
    }
    
    public Date getDateEndedAt(){
        if(this.endedAt == null) return null;
        return Date.from(this.endedAt.atZone(ZoneId.systemDefault()).toInstant());
    }

    public void setEndedAt(LocalDateTime endedAt) {
        this.endedAt = endedAt;
    }

    public void setEndedAt(Timestamp endedAt) {
        if(endedAt != null){
            this.endedAt = endedAt.toLocalDateTime();
        }else{
            this.endedAt = null;
        }
    }

    public void setEndedAt(Date endedAt) {
        if(endedAt != null){
            this.endedAt = endedAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }else{
            this.endedAt = null;
        }
    }
}
