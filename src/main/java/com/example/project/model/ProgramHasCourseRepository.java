package com.example.project.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.*;
public interface ProgramHasCourseRepository extends JpaRepository<ProgramHasCourse, Long> {
	
	
	List<ProgramHasCourse> findByIdIn(List<Long> ids);
	
	@Query("SELECT p FROM ProgramHasCourse p WHERE p.program1.id = :programId")
    List<ProgramHasCourse> findByProgramId(Long programId);

}
