package indi.jackwan.oleducation.repositories;

import indi.jackwan.oleducation.models.User;
import org.springframework.data.repository.CrudRepository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
public interface UserRepository extends CrudRepository<User, Long> {
    User findById(int id);
    User findByEmail(String email);
    User findByConfirmationToken(String confirmationToken);
}