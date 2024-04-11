package com.example.project.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;

import java.util.*;
public interface ProgramHasCourseRepository extends JpaRepository<ProgramHasCourse, Long> {
	
	
	List<ProgramHasCourse> findByIdIn(List<Long> ids);
	
	@Query("SELECT p FROM ProgramHasCourse p WHERE p.program1.id = :programId")
    List<ProgramHasCourse> findByProgramId(Long programId);
	@Transactional
	@Modifying
	@Query("DELETE from ProgramHasCourse where course1.id = :id")
	void deleteByCouseId(Long id);

}
