package com.example.project;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
	private final String pg1 = "Emerging Technology";
	private ArrayList<Program> programs;
	private ArrayList<Course> courses;
	private ArrayList<User> users;
	private ArrayList<ProgramHasCourse> programHasCourses;
	private Program emergeTech;
	public static void main(String[] args) {
		SpringApplication.run(Csis3275071G1ProjectApplication.class, args);
	}
	
	@Bean
	ApplicationRunner init (CourseRepository courseRepo, UserRepository userRepo
			, UserTakeCourseRepository userTakeCourseRepo, ProgramRepository programRepo,
			ProgramHasCourseRepository programHasCourseRepo) {
		return args -> {
			
			//add 3 program
	        addPrograms(programRepo);
	        
			//add sample data for the courses and the prerequisitives
			addCourses(courseRepo);
			
			//add test data for the user
			addUsers(userRepo);

			// Find Tom from the list of users
	        User tom = users.stream().filter(user -> user.getUsername().equals("Tom")).findFirst().orElse(null);
	        addCourseToUser(tom, userTakeCourseRepo);
	        
	        //add required courses to emergingTech program
	        List<String> emergingTech = Arrays.asList("CSIS1175", "CSIS1280", "CSIS2175", "CSIS2200", "CSIS2260",
	        		"CSIS2270", "CSIS2300", "CSIS3155", "CSIS3175", "CSIS3275", 
	        		"CSIS3280", "CSIS3375", "CSIS3380", "CSIS3475", "CSIS4175", 
	        		"CSIS4270", "CSIS4280", "CSIS4495");
	        addCourseToProgram(programHasCourseRepo, emergingTech);
			
		};
	}
	
	//add program to program repository
	public void addPrograms(ProgramRepository pr) {
		programs = new ArrayList<>();
        programs.add(new Program(1, pg1));
        programs.add(new Program(2,"Data Analytics"));
        programs.add(new Program(3,"CyberSecurity"));
        pr.saveAll(programs);
	}
	
	//add course to course repository
	public void addCourses(CourseRepository cr) {
		courses = new ArrayList<>();
		
		courses.add(new Course("BUSN2429", "Businesss Statistics"));	//0
		courses.add(new Course("CSIS1175", "Introduction to Programming"));
		courses.add(new Course("CSIS1280", "Multimedia Web Development"));
		courses.add(new Course("CSIS2175", "Adv Integrated Software Dev"));
		courses.add(new Course("CSIS2200", "Systems Analysis & Design"));
		
		courses.add(new Course("CSIS2260", "Operating Systems")); //5
		courses.add(new Course("CSIS2270", "Virtualization and Computer Netowrking"));
		courses.add(new Course("CSIS2300", "Database I"));	
		courses.add(new Course("CSIS3155", "IT Secturity Fundamentals"));
		courses.add(new Course("CSIS3160", "Evidence Imaging"));
		
		courses.add(new Course("CSIS3175", "Introduction to Mobile App Dev"));	//10
		courses.add(new Course("CSIS3275", "Software Engineering"));
		courses.add(new Course("CSIS3280", "Backend Web Development"));
		courses.add(new Course("CSIS3290", "Funda of Machine Learning"));
		courses.add(new Course("CSIS3375", "UX Design in Mobile Applications"));
		
		courses.add(new Course("CSIS3380", "Advanced Web Programming with JavaScript and AJAX"));	//15
		courses.add(new Course("CSIS3475", "Data Structures and Algorithms"));
		courses.add(new Course("CSIS4175", "Mobile Application Development II"));
		courses.add(new Course("CSIS4270", "Cloud Infrastructure"));
		courses.add(new Course("CSIS4280", "Special Topics in Emerging Technology"));
		courses.add(new Course("CSIS4495", "Applied Research Project"));	//20
		
															//|course| |pre-requisite|
		courses.get(3).setRequisitesOf(Set.of(2L)); 		//	2175	1175
		courses.get(7).setRequisitesOf(Set.of(5L)); 		//	2300	2200
		courses.get(8).setRequisitesOf(Set.of(6L, 7L)); 	//	3155 	2260, 2270
		courses.get(10).setRequisitesOf(Set.of(4L));		//	3175	2175
		courses.get(11).setRequisitesOf(Set.of(4L, 5L));	//	3275	2175, 2200
		courses.get(12).setRequisitesOf(Set.of(3L));		//	3280	1280
		courses.get(14).setRequisitesOf(Set.of(3L, 11L));	//	3375	1280, 3175
		courses.get(12).setRequisitesOf(Set.of(3L));		//	3380	1280
		courses.get(16).setRequisitesOf(Set.of(4L));		//	3475	2175
		courses.get(17).setRequisitesOf(Set.of(11L));		//	4175	3175
		courses.get(18).setRequisitesOf(Set.of(9L));		//	4270	3155
		courses.get(19).setRequisitesOf(Set.of(3L, 11L));	//	4280	1280, 3175
		courses.get(20).setRequisitesOf(Set.of(8L, 12L));	//	4495	2300, 3275
		cr.saveAll(courses);
	}
	
	//add user to user repository
	public void addUsers(UserRepository ur) {
		emergeTech = programs.stream().filter(pg -> pg.getDescription().equals(pg1)).findFirst().orElse(null);
		User student_ray = new User("student_ray", "what", false, emergeTech);
		users = new ArrayList<>();
		users.add(new User("Dang","1234B",true));
		users.add(new User("Yi-Sin","1234Y",true));
		users.add(new User("Raymond","1234R",true));
		users.add(new User("Tom","1234T",false, emergeTech));
		users.add(student_ray);
		ur.saveAll(users);
	}
	
	//add user taking course to UserTakeCourse repository
	public void addCourseToUser(User user, UserTakeCourseRepository utcr) {
		// Find CSIS2260 and CSIS2270 from the list of courses
        Course csis2260 = courses.stream().filter(course -> course.getCode().equals("CSIS2260")).findFirst().orElse(null);
        Course csis2270 = courses.stream().filter(course -> course.getCode().equals("CSIS2270")).findFirst().orElse(null);
        Course csis3155 = courses.stream().filter(course -> course.getCode().equals("CSIS3155")).findFirst().orElse(null);
        // Create UserTakeCourse instance for Tom and CSIS2260
        utcr.save(new UserTakeCourse(user, csis2260, false));
        utcr.save(new UserTakeCourse(user, csis2270, true));
        utcr.save(new UserTakeCourse(user, csis3155, false));
	}
	
	//add require courses to ProgramHasCourses repository
	public void addCourseToProgram(ProgramHasCourseRepository programHasCourseRepo, List<String> courseCodes) {
		//add key:value from all courses list to HashMap
		Map<String, Course> courseMap = new HashMap<>();
        for (Course course : courses) {
            courseMap.put(course.getCode(), course);
        }
        
        //add course to programHasCourses where code match in Map
        List<ProgramHasCourse> programHasCourses = new ArrayList<>();
        for (String code : courseCodes) {
            Course course = courseMap.get(code);
            if (course != null) {
                programHasCourses.add(new ProgramHasCourse(emergeTech, course));
            }
        }
        
        programHasCourses.add(new ProgramHasCourse(programs.get(1),courses.get(0)));
        programHasCourses.add(new ProgramHasCourse(programs.get(1),courses.get(1)));
        programHasCourses.add(new ProgramHasCourse(programs.get(1),courses.get(2)));
        programHasCourses.add(new ProgramHasCourse(programs.get(1),courses.get(4)));
        
        programHasCourses.add(new ProgramHasCourse(programs.get(2),courses.get(0)));
        programHasCourses.add(new ProgramHasCourse(programs.get(2),courses.get(1)));
        programHasCourses.add(new ProgramHasCourse(programs.get(2),courses.get(2)));
        programHasCourses.add(new ProgramHasCourse(programs.get(2),courses.get(5)));
        
        //save data to programHasCourse repository
        programHasCourseRepo.saveAll(programHasCourses);
	}
}
