package com.example.project.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.project.model.Course;
import com.example.project.model.CourseRepository;
import com.example.project.response.CourseResponse;
import com.example.project.response.CourseStatus;

import jakarta.transaction.Transactional;

@Service
public class CourseService {

	@Autowired
	CourseRepository courseRepository;
	@Autowired
	UserTakeCourseService userTakeCourseService;
	
	@Transactional
    public void deleteRequisiteIdFromCourses(Long requisiteId) {
        List<Course> courses = courseRepository.findAll();
        for (Course course : courses) {
            if (course.getRequisitesOf().remove(requisiteId)) {
                courseRepository.save(course);
            }
        }
    }
	

	public CourseResponse getOne(Long id, long userId) {

		Course findOne = courseRepository.findById(id).get();
		ArrayList<CourseResponse> pres = new ArrayList<>();
		if(findOne != null) {
			for(Long i : findOne.getRequisitesOf()) {
				Course pre = courseRepository.findById(i).get();
				if(pre != null) {
					pres.add(new CourseResponse(pre.getId(), pre.getCode(), pre.getTitle()));
				}
				
			}
			CourseResponse result = new CourseResponse(findOne.getId(),findOne.getCode(), findOne.getTitle());
			result.setStatus(userTakeCourseService.setStatus(id, userId));
			result.setRequisitesOf(pres);
			
			return result;
		} else {
			return null;
		}


	}

	private CourseResponse findOneCourseResponse(List<CourseResponse> result, Long id) {
//		System.out.println("findOne: " + ree.getId());
		for (CourseResponse response : result) {
			if (response.getId() == id) {
				return response;
			}
		}
		return null;
	}
	
//	private List<Long> getParentCourse(List<Long> result, Long id) {
//		try {
//			
//			
//			
//		}catch(Exception ex) {
//			return null;
//		}
//	}
	//find course by course response; if found, add course to ids list
	private CourseResponse getPreRequisites(CourseResponse courseResponse, List<Long> ids) {
		//get course by courseResponse Id
		Course c = courseRepository.findById(courseResponse.getId()).get();
		//if course has pre-requisite
		if (c.getRequisitesOf().size() > 0) {
			for (Long i : c.getRequisitesOf()) {
				//find each pre-requisite course
				Course tmp = courseRepository.findById(i).get();
				//add id to list
				ids.add(i);
				List<Long> buffer = new ArrayList<>();
				courseResponse.getRequisitesOf().forEach(x -> buffer.add(x.getId()));
				if(!buffer.contains(i)) {
					courseResponse.addRequisity(new CourseResponse(tmp.getId(), tmp.getCode(), tmp.getTitle()));
				}
			}
		}
		return courseResponse;

	}
	
	

	private void findPrerequisitesRecursive(List<CourseResponse> allCourse, List<CourseResponse> result, CourseResponse courseResponse,
			List<Long> ids) {
//		System.out.println("findPrerequisitesRecursive: " + courseResponse.getId() + courseResponse.getRequisitesOf());
		//if this courseResponse has pre-requisites
		if (courseResponse.getRequisitesOf().size() > 0) {
			System.out.println("courseResponse" + courseResponse.getId());
			for (CourseResponse cr : courseResponse.getRequisitesOf()) {
				//add pre-requisites Id to ids list
				ids.add(cr.getId());
				//retrieve course by id
				Course course = courseRepository.findById(cr.getId()).get();
				for (long i : course.getRequisitesOf()) {
					System.out.println("i: " + i);
					//find course by Id in allCourse list
					CourseResponse buffer = findOneCourseResponse(allCourse, i);
					if(buffer !=null) {
						buffer = getPreRequisites(buffer, ids);
						List<Long> bufferIds = new ArrayList<>();
						cr.getRequisitesOf().forEach(x -> bufferIds.add(x.getId()));
//						if(bufferIds.con)
						if(!bufferIds.contains(buffer.getId())) {
							for(Long tt : bufferIds) {
								System.out.println("tt: " + tt);
							}
							cr.addRequisity(buffer);
							bufferIds.add(buffer.getId());
							ids.add(buffer.getId());
						} else {
							System.out.println("Duplicate: " + buffer.getId());
						}
						
//						System.out.println("outI: " + cr.getId() + "buffersize" + buffer.getRequisitesOf().size());
						findPrerequisitesRecursive(allCourse, result, buffer, ids);
					}
					

				}

			}
		}
	}

	public void findInherit(List<CourseResponse> allCourses, List<CourseResponse> result, List<Long> ids) {

		for (CourseResponse c : result) {
//			System.out.println("findInherit: " + c.getId());
			findPrerequisitesRecursive(allCourses, result, c, ids);
		}
	}

}
