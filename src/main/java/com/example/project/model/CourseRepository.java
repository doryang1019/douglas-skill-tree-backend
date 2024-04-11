package com.example.project.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;



public interface CourseRepository extends JpaRepository<Course, Long> {
	List<Course> findByTitle(String title);
	
	
	@Query("SELECT c FROM Course c WHERE LOWER(c.code) LIKE %:keyword% OR LOWER(c.title) LIKE %:keyword%")
    List<Course> findByCodeOrTitle(String keyword);
	
	List<Course> findByCode(String code);
	
	List<Course> findByTitleContaining(String subTitle);
	// course_prerequisites
	
	@Transactional
    @Modifying
    @Query("update Course c set c.requisitesOf = null where c.id = :courseId and :requisiteId member of c.requisitesOf")
    void deleteRequisiteIdFromCourse(Long courseId, Long requisiteId);
	
	@Query("SELECT c.id FROM Course c WHERE :requisiteId MEMBER OF c.requisitesOf")
    List<Long> findCourseIdByRequisiteId(@Param("requisiteId") Long requisiteId);
	
	
	
	


}
