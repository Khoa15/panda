package model;

import java.time.LocalDate;

public class Collection {

    private int id;
    private String name;
    private int parentId;

    public Collection() {
        id = -1;
    }
    public Collection(String name) {
        id = -1;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

}
