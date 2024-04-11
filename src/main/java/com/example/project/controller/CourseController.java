package com.example.project.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.project.model.Course;
import com.example.project.model.CourseRepository;
import com.example.project.model.ProgramHasCourse;
import com.example.project.model.ProgramHasCourseRepository;
import com.example.project.model.ProgramRepository;
import com.example.project.model.UserTakeCourse;
import com.example.project.response.CourseResponse;
import com.example.project.response.CourseStatus;
import com.example.project.service.CourseService;
import com.example.project.service.UserTakeCourseService;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/course")
public class CourseController {

	@Autowired
	CourseRepository courseRepository;
	
	@Autowired
	ProgramRepository programRepository;
	@Autowired
	ProgramHasCourseRepository programHasCourseRepository;
	
	@Autowired
	UserTakeCourseService userTakeCourseService;
	

	@Autowired
	CourseService courseService;
	
	@GetMapping("/{id}/{userId}")
	public ResponseEntity<CourseResponse> getOneByUser(@PathVariable("id") long id, @PathVariable("userId") long userId) {
		try {

			CourseResponse cr = courseService.getOne(id, userId);
			if (cr != null) {
				return new ResponseEntity<>(cr, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("/{id}")
	public ResponseEntity<CourseResponse> getOne(@PathVariable("id") long id) {
		try {

			CourseResponse cr = courseService.getOne(id, -1);
			if (cr != null) {
				return new ResponseEntity<>(cr, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@GetMapping("all")
	public ResponseEntity<List<Course>> getAllUnformat() {
		try {
			List<Course> courses = courseRepository.findAll();
			return new ResponseEntity<>(courses, HttpStatus.OK);
			
		}catch(Exception ex) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	//prepare course response
	public void formatCourse(List<Course> courses, List<CourseResponse> result, Long userId) {
//		System.out.println("course size" + courses.get(0).getCode());
//		System.out.print("result" + result.size());
		for(Course c: courses) {
//			System.out.println("go th for loop");
//			System.out.println(c.getId());
			//get c status
			CourseStatus status = userTakeCourseService.setStatus(c.getId(), userId);
			CourseResponse cr = new CourseResponse(c.getId(), c.getCode(), c.getTitle(), status);
			for(Long i : c.getRequisitesOf()) {
				//get pre-requisites
				Course tmp = courseRepository.findById(i).get();
				//check pre-requisites status
				CourseStatus status2 = userTakeCourseService.setStatus(tmp.getId(), userId);
				//add pre-requisites course response 
				cr.addRequisity(new CourseResponse(tmp.getId(), tmp.getCode(), tmp.getTitle(), status2));
			}
			//add each course response to result list
			result.add(cr);
		}
	}
	//given a program, return all courses associate with that program
    public List<Course> getCourseByProgram(@PathVariable("id") long id) {
		
		try {
			List<Course> courses= new ArrayList<>();
			
			List<ProgramHasCourse> buffer = programHasCourseRepository.findByProgramId(id);
			if(buffer.size() > 0 ) {
				
				buffer.forEach(x -> {
					Optional<Course> course = courseRepository.findById(x.getCourse1().getId());
					if(course.isPresent()) {
						courses.add(course.get());
					}
				});
				return courses;
				
			}else {
				return null;
			}
			
		} catch(Exception e) {
			return null;
		}
		
	}
    
    //get courses
	@GetMapping("")
	public ResponseEntity<List<CourseResponse>> getAllCourses(
			@RequestParam(name = "key", required = false) String key ,
			@RequestParam(name="program", required = false) Long programId,
			@RequestParam(name="userId", required=true) Long userId) {
		try {
			System.out.print(key);
//			System.out.print("program: " + programId);
			List<Course> courses = new ArrayList<>();
			List<Course> orgCourses = new ArrayList<>();
			List<CourseResponse> result = new ArrayList<>();
			List<CourseResponse> allCourses = new ArrayList<>();
			
			if (key == null || key.length() ==0 || key.isEmpty()) {
				//no key, no programId
				if(programId == null) {
					System.out.print("!!!!NONE!!!!!");
					courseRepository.findAll().forEach(courses::add);
				} else { //no key, have programId
					System.out.print("!!!!NONE2!!!!!");
					courses = getCourseByProgram(programId);
					System.out.print("Course size " + courses.get(0).getCode());
					courses.forEach((x) -> System.out.println(x.getTitle()));
				}
				
			} else { 
				if(programId == null) { //have key, no programId
					System.out.print("!!!!NONE3!!!!!");
					courseRepository.findByCodeOrTitle(key.toLowerCase()).stream().forEach(courses::add);
				} else { //have key, have programId
					System.out.print("HAS PROGRAM");
					courses = getCourseByProgram(programId).stream().filter(x -> x.getCode().toLowerCase().contains(key.toLowerCase()) ||
							x.getTitle().toLowerCase().contains(key.toLowerCase())).toList();
//					courseRepository.findByCodeOrTitle(key.toLowerCase()).stream().filter(x -> x.).forEach(courses::add);
				}
				
			}
			courseRepository.findAll().forEach(orgCourses::add);
			ArrayList<Long> ids = new ArrayList<>();
			formatCourse(orgCourses, allCourses, userId);
			formatCourse(courses, result, userId);
			courseService.findInherit(allCourses, result, ids);
			return new ResponseEntity<>(
					result.stream().filter(x -> !ids.contains(x.getId())).collect(Collectors.toList()), HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("")
	public ResponseEntity<Course> addCourse(@RequestBody Course course) {
		try {

			List<Course> courses = courseRepository.findByTitle(course.getTitle());
			if (courses.size() > 0) {
				return new ResponseEntity<>(HttpStatus.CONFLICT);
			} else {
				if (course.getCode() != null && course.getTitle() != null) {
					return new ResponseEntity<>(courseRepository.save(course), HttpStatus.CREATED);
				} else {
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Course> deleteCourse(@PathVariable("id") long id) {
		try {

			Course findOne = courseRepository.findById(id).get();
			if (findOne != null) {
				System.out.print("size: " + findOne.getRequisitesOf().size());
				if(findOne.getRequisitesOf().size() !=0) {
					return new ResponseEntity<>(HttpStatus.FORBIDDEN);
				}else {
					courseRepository.delete(findOne);
					return new ResponseEntity<>(findOne, HttpStatus.OK);
				}
				
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	@PutMapping("/{id}")
	public ResponseEntity<Course> updateCourse(@RequestBody Course course, @PathVariable("id") long id) {
		try {

			Course findOne = courseRepository.findById(id).get();
			if (findOne != null) {
				findOne.setCode(course.getCode());
				findOne.setTitle(course.getTitle());
				findOne.setRequisitesOf(course.getRequisitesOf());
				return new ResponseEntity<>(courseRepository.save(findOne), HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@GetMapping("/status/{programId}/{studentId}")
	public ResponseEntity<List<CourseResponse>> getCoursesByProgram(@PathVariable("programId") long programId, @PathVariable("studentId") long stuId){
		try {
			List<Course> courses;
			List<CourseResponse> result = new ArrayList<>();
			courses = getCourseByProgram(programId);
			formatCourse(courses, result, stuId);
			return new ResponseEntity<>(result, HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
