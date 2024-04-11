package com.example.project;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.project.model.Course;
import com.example.project.model.CourseRepository;
import com.example.project.model.Program;
import com.example.project.model.ProgramHasCourse;
import com.example.project.model.ProgramHasCourseRepository;
import com.example.project.model.ProgramRepository;
import com.example.project.model.User;
import com.example.project.model.UserRepository;
import com.example.project.model.UserTakeCourse;
import com.example.project.model.UserTakeCourseRepository;

@SpringBootApplication
public class Csis3275071G1ProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(Csis3275071G1ProjectApplication.class, args);
	}
	
	@Bean
	ApplicationRunner init (CourseRepository courseRepo, UserRepository userRepo
			, UserTakeCourseRepository userTakeCourseRepo, ProgramRepository programRepo,
			ProgramHasCourseRepository programHasCourseRepo) {
		return args -> {
			
			// add test data for the courses and the prerequisitives
			ArrayList<Course> courses = new ArrayList<>();
			
			courses.add(new Course("CSIS2260", "Operating Systems"));
			courses.add(new Course("CSIS2270", "Virtualization and Computer Netowrking"));
			courses.add(new Course("CSIS3155", "IT Secturity Fundamentals"));
			courses.add(new Course("CSIS3280", "BackEnd Web Development"));
			courses.add(new Course("BUSN2429", "Businesss Statistics"));
			courses.add(new Course("CSIS3160", "Evidence Imaging"));
			
			Set<Long>requisitesOf3155 = Set.of(1L, 2L);;
			
			courses.get(2).setRequisitesOf(requisitesOf3155);
			courses.get(5).setRequisitesOf(Set.of(3L));
			courseRepo.saveAll(courses);
			
			// add test data for the user
			
			ArrayList<User> users = new ArrayList<>();
			users.add(new User("Dang","1234B",true ));
			users.add(new User("Yi-Sin","1234Y",true ));
			users.add(new User("Reymond","1234R",true ));
			users.add(new User("Tom","1234T",false ));
			
			
			
			userRepo.saveAll(users);
			
			// Find Tom from the list of users
	        User tom = users.stream().filter(user -> user.getUsername().equals("Tom")).findFirst().orElse(null);
	        
	        // Find CSIS2260 and CSIS2270 from the list of courses
	        Course csis2260 = courses.stream().filter(course -> course.getCode().equals("CSIS2260")).findFirst().orElse(null);
	        Course csis2270 = courses.stream().filter(course -> course.getCode().equals("CSIS2270")).findFirst().orElse(null);
	        Course csis3155 =  courses.stream().filter(course -> course.getCode().equals("CSIS3155")).findFirst().orElse(null);
	        // Create UserTakeCourse instance for Tom and CSIS2260
	        UserTakeCourse userTakeCourse1 = new UserTakeCourse(tom, csis2260, false);
	        userTakeCourseRepo.save(userTakeCourse1);

	        // Create UserTakeCourse instance for Tom and CSIS2270
	        UserTakeCourse userTakeCourse2 = new UserTakeCourse(tom, csis2270, true);
	        UserTakeCourse ut3 = new UserTakeCourse(tom, csis3155, false);
	        userTakeCourseRepo.save(userTakeCourse2);
	        userTakeCourseRepo.save(ut3);
	        
	        
	        
	     // add 3 program
	        ArrayList<Program> programs = new ArrayList<>();
	        programs.add(new Program(1,"Emerging Technology"));
	        programs.add(new Program(2,"Data Analytics"));
	        programs.add(new Program(3,"CyberSecurity"));
	        
	        programRepo.saveAll(programs);
	        
	        Program emergeTech = programs.get(0);
	        
	        users.get(3).setProgram(emergeTech);
	        userRepo.saveAll(users);
	        
	     // assign 3 courses to the program
	        
	        ArrayList<ProgramHasCourse> programHasCourses = new ArrayList<>();
	        
	        programHasCourses.add(new ProgramHasCourse(programs.get(0),courses.get(0)));
	        programHasCourses.add(new ProgramHasCourse(programs.get(0),courses.get(1)));
	        programHasCourses.add(new ProgramHasCourse(programs.get(0),courses.get(2)));
	        programHasCourses.add(new ProgramHasCourse(programs.get(0),courses.get(3)));
	        
	        programHasCourses.add(new ProgramHasCourse(programs.get(1),courses.get(0)));
	        programHasCourses.add(new ProgramHasCourse(programs.get(1),courses.get(1)));
	        programHasCourses.add(new ProgramHasCourse(programs.get(1),courses.get(2)));
	        programHasCourses.add(new ProgramHasCourse(programs.get(1),courses.get(4)));
	        
	        programHasCourses.add(new ProgramHasCourse(programs.get(2),courses.get(0)));
	        programHasCourses.add(new ProgramHasCourse(programs.get(2),courses.get(1)));
	        programHasCourses.add(new ProgramHasCourse(programs.get(2),courses.get(2)));
	        programHasCourses.add(new ProgramHasCourse(programs.get(2),courses.get(5)));
	        
	        programHasCourseRepo.saveAll(programHasCourses);
			
			
		};
	}

}
