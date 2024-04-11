package com.example.project.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.project.model.Course;
import com.example.project.model.CourseRepository;
import com.example.project.model.User;
import com.example.project.model.UserRepository;
import com.example.project.model.UserTakeCourse;
import com.example.project.model.UserTakeCourseRepository;
import com.example.project.response.CourseStatus;

@Service
public class UserTakeCourseService {

	@Autowired
	UserTakeCourseRepository userTakeCourseRepository;
	
	
	UserRepository userRepository;
	
	CourseRepository courseRepository;
	
	// return course status
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
	
	
	public CourseStatus updateStatus(long courseId, long userId, boolean newIsTaken, boolean newIsDone) {
		
		// check if exist course with that userID
		UserTakeCourse result = userTakeCourseRepository.findByUserIdAndCourseId(userId, courseId);
		
		// check if user exist
		Optional<User> searchUser = userRepository.findById(userId);
		
		//check if course exist
		Optional<Course> searchCourse = courseRepository.findById(courseId);
		
		
		if (searchUser.isPresent() && searchCourse.isPresent()) {
			//course does not exist in table usertakecourse
			if (result == null && newIsTaken ==true && newIsDone == true) {
				UserTakeCourse newInput = new UserTakeCourse (searchUser.get(),searchCourse.get(),true);
				userTakeCourseRepository.save(newInput);
				return new CourseStatus(newIsTaken, newIsDone);
			}
			else if (result == null && newIsTaken ==true && newIsDone == false) {
				UserTakeCourse newInput = new UserTakeCourse (searchUser.get(),searchCourse.get(),false);
				userTakeCourseRepository.save(newInput);
				return new CourseStatus(newIsTaken, newIsDone);
			}
			// course does exist in the table
			else if (result!=null) {
				if( newIsTaken ==true && newIsDone == true) {
					result.setDone(true);
				}
				else if(newIsTaken ==true && newIsDone == false)
				{
					result.setDone(false);
				}
				return new CourseStatus(newIsTaken, newIsDone);
			}
		}
		
		// Return null if either user or course not found
		
		return null;
		
		// Find Tom from the list of users
        
		
		
		
	}
	
	
}
