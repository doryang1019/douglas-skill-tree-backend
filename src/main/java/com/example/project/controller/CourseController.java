package com.example.project.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.project.model.Course;
import com.example.project.model.CourseRepository;
import com.example.project.model.UserTakeCourse;
import com.example.project.response.CourseResponse;
import com.example.project.service.CourseService;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/course")
public class CourseController {

	@Autowired
	CourseRepository courseRepository;

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
	
	public void formatCourse(List<Course> courses, List<CourseResponse> result) {
		for(Course c: courses) {
//			System.out.println("go th for loop");
//			System.out.println(c.getId());
			CourseResponse cr = new CourseResponse(c.getId(), c.getCode(), c.getTitle());
			for(Long i : c.getRequisitesOf()) {
				Course tmp = courseRepository.findById(i).get();
				cr.addRequisity(new CourseResponse(tmp.getId(), tmp.getCode(), tmp.getTitle()));
			}
			result.add(cr);
		}
	}

	@GetMapping("")
	public ResponseEntity<List<CourseResponse>> getAllCourses(
			@RequestParam(name = "key", required = false) String key) {
		try {
			System.out.print(key);
			List<Course> courses = new ArrayList<>();
			List<Course> orgCourses = new ArrayList<>();
			List<CourseResponse> result = new ArrayList<>();
			List<CourseResponse> allCourses = new ArrayList<>();
			if (key == null) {
				courseRepository.findAll().forEach(courses::add);
			} else {
				courseRepository.findByCodeOrTitle(key.toLowerCase()).stream().forEach(courses::add);
			}
			courseRepository.findAll().forEach(orgCourses::add);
			ArrayList<Course> pres = new ArrayList<>();
			ArrayList<Long> ids = new ArrayList<>();
			formatCourse(orgCourses, allCourses);
			formatCourse(courses, result);
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

}
