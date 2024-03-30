package com.example.project.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="courses")
public class Course {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "code")
	private String code;

	@Column(name = "title")
	private String title;
	
	

	//recursive relationship -> something is off, need to recheck
	
	@ElementCollection
    @CollectionTable(name = "course_prerequisites", joinColumns = @JoinColumn(name = "course_id"))
    @Column(name = "prerequisite_id")
    private Set<Long> requisitesOf = new HashSet<>();
	
	
	// many to many relationship between course and user
	@OneToMany(mappedBy="course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	public List<UserTakeCourse> relations = new ArrayList<>();
	
	
	// many to many relationship between course and program
	@OneToMany(mappedBy="course1",  cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	public List<ProgramHasCourse> relations1 = new ArrayList<>();
	
	
	public Course() {}

	public Course(String code, String title) {
		super();
		this.code = code;
		this.title = title;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<Long> getRequisitesOf() {
		return requisitesOf;
	}

	public void setRequisitesOf(Set<Long> requisitesOf) {
		this.requisitesOf = requisitesOf;
	}

	public List<UserTakeCourse> getRelations() {
		return relations;
	}

	public void setRelations(List<UserTakeCourse> relations) {
		this.relations = relations;
	}

	public List<ProgramHasCourse> getRelations1() {
		return relations1;
	}

	public void setRelations1(List<ProgramHasCourse> relations1) {
		this.relations1 = relations1;
	}
	
	
	
	
	
	
}
