package com.example.project.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



public interface CourseRepository extends JpaRepository<Course, Long> {
	List<Course> findByTitle(String title);
	
	
	@Query("SELECT c FROM Course c WHERE LOWER(c.code) LIKE %:keyword% OR LOWER(c.title) LIKE %:keyword%")
    List<Course> findByCodeOrTitle(String keyword);
	
	List<Course> findByCode(String code);
	
	List<Course> findByTitleContaining(String subTitle);
}
