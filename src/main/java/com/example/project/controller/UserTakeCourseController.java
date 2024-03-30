package com.example.project.controller;

import java.util.ArrayList;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.project.model.Course;
import com.example.project.model.UserTakeCourse;
import com.example.project.model.UserTakeCourseRepository;
import com.example.project.response.CourseResponse;


@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/users")
public class UserTakeCourseController {
	
	@Autowired
	UserTakeCourseRepository userTakeCourseRepository;
	
	@GetMapping("/{userId}")
	public ResponseEntity<List<UserTakeCourse>>getOne(@PathVariable("userId") long id){
		try {
			
			List<UserTakeCourse> findAllCourses = userTakeCourseRepository.findByUserId(id);
			System.out.println(findAllCourses);
			if(findAllCourses.size() !=0) {
				userTakeCourseRepository.findAll().forEach(findAllCourses::add);
				return new ResponseEntity<>(findAllCourses,HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			

		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		} 
		
	}
	
	

}
