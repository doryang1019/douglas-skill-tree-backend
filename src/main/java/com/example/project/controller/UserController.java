package com.example.project.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.project.model.*;
import com.example.project.model.CourseRepository;
import com.example.project.model.User;
import com.example.project.model.UserRepository;
import com.example.project.request.UserLoginRequest;
import com.example.project.request.*;

import java.util.Optional;
import com.example.project.response.MessageResponse;

import java.util.*;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/user")
public class UserController {
	
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	ProgramRepository programRepository;
	
	
	@PutMapping("")
	public ResponseEntity<User> updatePassword(
			@RequestBody User user){
		try {
			User findOne = userRepository.findById(user.getId()).get();
			if (findOne != null ) {
				findOne.setPassword(user.getPassword());
				return new ResponseEntity<>(userRepository.save(findOne), HttpStatus.OK);

			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}


		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
			
		
	}
	
	@PostMapping("/add") 
	public ResponseEntity<User> addUser(@RequestBody UserAddRequest req ) {
		try {
			Optional<Program> program = programRepository.findById(req.getProgramId());
			if(program.isPresent() && !req.getUserName().isEmpty() && !req.getPassword().isEmpty()) {
				User user = new User(req.getUserName(), req.getPassword(), req.isAdmin(), program.get());
				User result = userRepository.save(user);
				return new ResponseEntity<>(result, HttpStatus.OK);
			} else {
				MessageResponse msg = new MessageResponse("One of the options is empty");
				return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
			}
			
			
		} catch(Exception e) {
			return null;
		}
		
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserLoginRequest loginRequest) {

		try {
			Optional<User> userData = userRepository.findByUsername(loginRequest.getUserID());
			if (userData.isPresent()) {
				String password = userData.get().getPassword();
				if (password.equals(loginRequest.getPassword())) {
					return new ResponseEntity<>(userData.get(), HttpStatus.OK);
				}
				MessageResponse msg = new MessageResponse("Incorrect password");
				return new ResponseEntity<>(msg, HttpStatus.FORBIDDEN);
			}
			MessageResponse msg = new MessageResponse("No such user");
			return new ResponseEntity<>(msg, HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			MessageResponse msg = new MessageResponse("Server Error");
			return new ResponseEntity<>(msg, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	
	
	

}
