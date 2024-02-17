package model;

public class Flashcard extends Card {
	private String front;
	private String back;

	public Flashcard() {
		super();
	}
	
	public String getFront() {
		return front;
	}

	public void setFront(String front) {
		this.front = front;
	}

	public String getBack() {
		return back;
	}

	public void setBack(String back) {
		this.back = back;
	}
}
