package com.example.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.model.Program;
import com.example.project.model.ProgramRepository;
import com.example.project.model.User;
import com.example.project.request.UserAddRequest;
import java.util.*;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/program")
public class ProgramController {
	
	@Autowired
	ProgramRepository programRepository;
	
	@GetMapping("") 
	public ResponseEntity<List<Program>> getAllProgram( ) {
		try {
			List<Program> program = programRepository.findAll();
			
			return new ResponseEntity<>(program, HttpStatus.OK);
			
		} catch(Exception e) {
			return null;
		}
		
	}

}
