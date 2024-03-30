package com.example.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="users_take_course")
public class UserTakeCourse {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	
	@ManyToOne
	@JoinColumn(name="course_id")
	private Course course;
	
	@JoinColumn(name="isDone")
	private boolean isDone;
	
	public UserTakeCourse() {}
	
	

	public UserTakeCourse(User userId, Course courseId, boolean isDone) {
		super();
		this.user = userId;
		this.course = courseId;
		this.isDone = isDone;
	}



	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isDone() {
		return isDone;
	}

	public void setDone(boolean isDone) {
		this.isDone = isDone;
	}



	public User getUser() {
		return user;
	}



	public void setUser(User user) {
		this.user = user;
	}



	public Course getCourse() {
		return course;
	}



	public void setCourse(Course course) {
		this.course = course;
	}
	
	
	
	

}
