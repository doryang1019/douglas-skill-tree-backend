package com.example.project.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="programs")
public class Program {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	

	
// assume only doing for PBBcis program
	
	@Column(name = "stream")
	private int stream; // 1 - emerge tech 2- data analytics 3- cybersecurity
	
	@Column(name = "description")
	private String description;
	
	//relationship between program and users
	@OneToMany(mappedBy = "program", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	private Set<User> users = new HashSet<>();
	
	
	//many to many between program and course via program_has_course
	@OneToMany(mappedBy="program1")
	@JsonIgnore
	public List<ProgramHasCourse> relations1 = new ArrayList<>();
	
	
	
	public Program() {}



	public Program(int stream) {
		super();
		this.stream = stream;
	}

	

	public Program(int stream, String description) {
		super();
		this.stream = stream;
		this.description = description;
	}



	public void addUser(User user) {
		this.users.add(user);
		user.setProgram(this);
	}

	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public int getStream() {
		return stream;
	}



	public void setStream(int stream) {
		this.stream = stream;
	}



	public Set<User> getUsers() {
		return users;
	}



	public void setUsers(Set<User> users) {
		this.users = users;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public List<ProgramHasCourse> getRelations1() {
		return relations1;
	}



	public void setRelations1(List<ProgramHasCourse> relations1) {
		this.relations1 = relations1;
	}
	
	
	
	
	
}
