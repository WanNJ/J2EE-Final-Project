package indi.jackwan.oleducation.repositories;

import indi.jackwan.oleducation.models.Organization;
import indi.jackwan.oleducation.models.User;
import indi.jackwan.oleducation.models.UserOrder;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<UserOrder, Long> {
    UserOrder findById(int id);
    List<UserOrder> findUserOrdersByOrganization(Organization organization);
    List<UserOrder> findUserOrdersByUser(User user);
}
