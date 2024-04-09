package com.example.project.response;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.project.model.Course;
import com.example.project.model.UserTakeCourse;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

public class CourseResponse {
	

	private long id;

	private String code;

	private String title;
	
	private CourseStatus status;
	
	

	public CourseStatus getStatus() {
		return status;
	}

	public void setStatus(CourseStatus status) {
		this.status = status;
	}

	private List<CourseResponse> requisitesOf = new ArrayList<>();
	
	public CourseResponse() {}

	public CourseResponse(Long id, String code, String title) {
		super();
		this.id = id;
		this.code = code;
		this.title = title;
//		this.requisitesOf = requisitesOf;
	}
	public CourseResponse(Long id, String code, String title, CourseStatus status) {
		super();
		this.id = id;
		this.code = code;
		this.title = title;
		this.status = status;
//		this.requisitesOf = requisitesOf;
	}

	public List<CourseResponse> getRequisitesOf() {
		return requisitesOf;
	}

	public void setRequisitesOf(List<CourseResponse> requisitesOf) {
		this.requisitesOf = requisitesOf;
	}
	
	public void addRequisity(CourseResponse c) {
		this.requisitesOf.add(c);
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



}
