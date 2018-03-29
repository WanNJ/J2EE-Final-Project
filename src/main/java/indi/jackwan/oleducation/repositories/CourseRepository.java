package indi.jackwan.oleducation.repositories;

import indi.jackwan.oleducation.models.Course;
import indi.jackwan.oleducation.models.Organization;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CourseRepository extends CrudRepository<Course, Long> {
    List<Course> findAll();
    Course findById(int id);
    List<Course> findCoursesByOrganization(Organization organization);
}
