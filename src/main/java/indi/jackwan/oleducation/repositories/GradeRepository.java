package indi.jackwan.oleducation.repositories;

import indi.jackwan.oleducation.models.Class;
import indi.jackwan.oleducation.models.Grade;
import indi.jackwan.oleducation.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GradeRepository extends CrudRepository<Grade, Long> {
    Grade findById(int id);
    List<Grade> findGradesByAClass(Class aClass);
    List<Grade> findGradesByUser(User user);
}
