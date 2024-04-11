package com.example.project.request;

import com.example.project.model.Course;

public class AddCourseRequest {
	
	private Course course;
	
	private Long programId;

	public AddCourseRequest(Course course, Long programId) {
		super();
		this.course = course;
		this.programId = programId;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId(Long programId) {
		this.programId = programId;
	}

}
