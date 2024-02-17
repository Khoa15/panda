package model;

public class Task extends BaseClass {
	private int parentId;
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
	
	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public boolean isFullDay() {
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
        
        public String getStrIsDone(){
            return (isDone == true) ? "Done" : "Nope";
        }

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}
}
