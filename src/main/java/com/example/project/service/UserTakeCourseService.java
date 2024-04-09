package com.example.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.model.UserTakeCourse;
import com.example.project.model.UserTakeCourseRepository;
import com.example.project.response.CourseStatus;

@Service
public class UserTakeCourseService {

	@Autowired
	UserTakeCourseRepository userTakeCourseRepository;
	
	public CourseStatus setStatus(long courseId, long userId) {
		
	 UserTakeCourse result = userTakeCourseRepository.findByUserIdAndCourseId(userId, courseId);
//	 System.out.print(result.getCourse().getId());
//	 System.out.print(result.getUser().getId());
//	 
	 if(result == null) {
		 return new CourseStatus(false, false);
	 } else {
		 if(result.isDone()) {
			 return new CourseStatus(true, true);
		 } else {
			 return new CourseStatus(true, false);
		 }
	 }
	 
		
	}
	
	
}
