package com.example.project.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="program_has_course")
public class ProgramHasCourse {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	
	@ManyToOne
	@JoinColumn(name="program_id")
	private Program program1;
	
	
	@ManyToOne
	@JoinColumn(name="course_id")
	private Course course1;
	
	public ProgramHasCourse() {}

	public ProgramHasCourse(Program program1, Course course1) {
		super();
		this.program1 = program1;
		this.course1 = course1;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Program getProgram1() {
		return program1;
	}

	public void setProgram1(Program program1) {
		this.program1 = program1;
	}

	public Course getCourse1() {
		return course1;
	}

	public void setCourse1(Course course1) {
		this.course1 = course1;
	}
	
	

}
