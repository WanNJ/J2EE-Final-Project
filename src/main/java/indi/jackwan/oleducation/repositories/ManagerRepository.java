package indi.jackwan.oleducation.repositories;

import indi.jackwan.oleducation.models.Manager;
import org.springframework.data.repository.CrudRepository;

public interface ManagerRepository extends CrudRepository<Manager, Long> {
    Manager findByUsername(String username);
}
