package model;

import java.time.LocalDateTime;
import java.time.LocalDate;

public class Card {

    protected int id;
    protected byte memory;
    protected int nLearn;
    protected int nMissed;
    protected LocalDateTime learnNextTimeAt;
    private boolean isFlashcard;

    public Card() {
        memory = 0;
        nLearn = 0;
        nMissed = 0;
        isFlashcard = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte getMemory() {
        return memory;
    }

    public void setMemory(byte memory) {
        this.memory = memory;
    }

    public int getnLearn() {
        return nLearn;
    }

    public void setnLearn(int nLearn) {
        this.nLearn = nLearn;
    }

    public int getnMissed() {
        return nMissed;
    }

    public void setnMissed(int nMissed) {
        this.nMissed = nMissed;
    }

    public LocalDateTime getLearnNextTimeAt() {
        return learnNextTimeAt;
    }

    public void setLearnNextTimeAt(LocalDateTime learnNextTimeAt) {
        this.learnNextTimeAt = learnNextTimeAt;
    }

    public boolean getIsFlashcard() {
        return isFlashcard;
    }

    public void setIsFlashcard(boolean isFlashcard) {
        this.isFlashcard = isFlashcard;
    }

}
