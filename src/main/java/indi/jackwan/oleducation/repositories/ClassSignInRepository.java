package indi.jackwan.oleducation.repositories;

import indi.jackwan.oleducation.models.Class;
import indi.jackwan.oleducation.models.ClassSignIn;
import indi.jackwan.oleducation.models.Grade;
import indi.jackwan.oleducation.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@SuppressWarnings("SpringDataMethodInconsistencyInspection")
public interface ClassSignInRepository extends CrudRepository<ClassSignIn, Long> {
    ClassSignIn findById(int id);
    List<ClassSignIn> findAllByUserAndAClass(User user, Class aClass);
    List<ClassSignIn> findAllByUser(User user);
}
