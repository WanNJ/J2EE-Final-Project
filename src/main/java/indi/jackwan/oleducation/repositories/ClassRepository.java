package indi.jackwan.oleducation.repositories;

import indi.jackwan.oleducation.models.Class;
import indi.jackwan.oleducation.models.Course;
import indi.jackwan.oleducation.models.Organization;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ClassRepository extends CrudRepository<Class, Long>  {
    Class findById(int id);
    List<Class> findClassesByCourse(Course course);
    Class findByIdAndOrganization(int id, Organization organization);
}
