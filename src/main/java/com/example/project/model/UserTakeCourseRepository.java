package com.example.project.model;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface UserTakeCourseRepository extends JpaRepository<UserTakeCourse, Long> {
	
	List<UserTakeCourse> findByUserId(long userId);
	
	UserTakeCourse findByUserIdAndCourseId(long userId, long courseId);

}
