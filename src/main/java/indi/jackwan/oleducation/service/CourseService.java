package indi.jackwan.oleducation.service;

import indi.jackwan.oleducation.models.Class;
import indi.jackwan.oleducation.models.Course;
import indi.jackwan.oleducation.models.Organization;
import indi.jackwan.oleducation.repositories.ClassRepository;
import indi.jackwan.oleducation.repositories.CourseRepository;
import indi.jackwan.oleducation.repositories.OrganizationRepository;
import indi.jackwan.oleducation.utils.Enums.ReleaseCourseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service("courseService")
public class CourseService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private ClassRepository classRepository;

    public ReleaseCourseResult releaseCourse(Course course) {
        Organization organization = organizationRepository.findByOrgCode(course.getOrganization().getOrgCode());

        if(organization == null) {
            return ReleaseCourseResult.NO_SUCH_ORGANIZATION;
        } else {
            if (course.getStartTime().after(course.getEndTime()) || course.getStartTime().before(new Date())) {
                return ReleaseCourseResult.INVALID_TIME;
            } else {
                courseRepository.save(course);
                return ReleaseCourseResult.SUCCESS;
            }
        }
    }

    public boolean addClass(Organization organization, Course course, Class aClass) {
        if(aClass.getStudentNumber() < 0 || aClass.getTeacherName().equals("") || aClass.getPrice() < 0 )
            return false;
        else {
            aClass.setOrganization(organization);
            aClass.setCourse(course);
            classRepository.save(aClass);
            return true;
        }
    }

    public List<Course> findCoursesByOrganization(Organization organization) {
        return courseRepository.findCoursesByOrganization(organization);
    }

    public List<Class> getAllClass(Course course) {
        return classRepository.findClassesByCourse(course);
    }

    public Course findById(int id) {
        return courseRepository.findById(id);
    }
}
