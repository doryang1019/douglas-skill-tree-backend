package com.example.project.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="users")
public class User {

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;
	
	@Column(name = "isadmin")
	private boolean isAdmin;
	
	
	
	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "program_id", nullable = true)
	@JsonIgnore
	private Program program;
	
	
	// many to many between user and course via UserTakeCourse
	
	@OneToMany(mappedBy="user")
	public List<UserTakeCourse> relations = new ArrayList<>();
	
	
	
	
	public User() {}

	public User(String username, String password, boolean isAdmin) {
		super();
		this.username = username;
		this.password = password;
		this.isAdmin = isAdmin;
	}
	
	
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	public List<UserTakeCourse> getRelations() {
		return relations;
	}

	public void setRelations(List<UserTakeCourse> relations) {
		this.relations = relations;
	}

	
	
	
	
}
