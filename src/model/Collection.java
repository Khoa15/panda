package model;

import java.time.LocalDate;

public class Collection {

    private int id;
    private String name;
    private int totalCard;
    private float percentTrue;
    private float percentLevel5;
    private int parentId;

    public Collection() {
        id = -1;
        percentTrue = 0;
        percentLevel5 = 0;
        totalCard = 0;
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

    public int getTotalCard() {
        return totalCard;
    }

    public void setTotalCard(int totalCard) {
        this.totalCard = totalCard;
    }

    public float getPercentTrue() {
        return percentTrue;
    }

    public void setPercentTrue(float percentTrue) {
        this.percentTrue = percentTrue;
    }

    public float getPercentLevel5() {
        return percentLevel5;
    }

    public void setPercentLevel5(float percentLevel5) {
        this.percentLevel5 = percentLevel5;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

}
