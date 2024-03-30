package com.example.project.model;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramRepository extends JpaRepository<Program, Long> {

	List<Program> findByStream(int stream);
}
