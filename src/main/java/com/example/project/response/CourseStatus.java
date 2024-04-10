package com.example.project.response;

public class CourseStatus {
	
	private boolean isTaken;
	private boolean isDone;
	
	public boolean isTaken() {
		return isTaken;
	}

	public void setTaken(boolean isTaken) {
		this.isTaken = isTaken;
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}

	
	
	
	public CourseStatus(boolean isTaken, boolean isDone) {
		this.isDone = isDone;
		this.isTaken = isTaken;
	}

}
