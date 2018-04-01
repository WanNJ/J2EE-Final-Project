package indi.jackwan.oleducation.repositories;

import indi.jackwan.oleducation.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findAll();
    User findById(int id);
    User findByEmail(String email);
    User findByConfirmationToken(String confirmationToken);
    long count();
    @SuppressWarnings("SpringDataMethodInconsistencyInspection")
    int countUsersByEnabledAndIsVip(boolean enable, boolean isVip);
    int countUsersByExpenditureGreaterThan(double value);
}