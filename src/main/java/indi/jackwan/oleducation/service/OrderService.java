package indi.jackwan.oleducation.service;

import indi.jackwan.oleducation.models.*;
import indi.jackwan.oleducation.models.Class;
import indi.jackwan.oleducation.repositories.ClassRepository;
import indi.jackwan.oleducation.repositories.CourseRepository;
import indi.jackwan.oleducation.repositories.OrderRepository;
import indi.jackwan.oleducation.utils.Enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("orderService")
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private VipService vipService;

    public UserOrder findById(int id) {
        return orderRepository.findById(id);
    }

    public List<UserOrder> findUserOrdersByOrganization(Organization organization) {
        return orderRepository.findUserOrdersByOrganization(organization);
    }

    public List<UserOrder> findUserOrdersByUser(User user) {
        return orderRepository.findUserOrdersByUser(user);
    }

    public boolean makeClassReservation(User user, int classId, UserOrder userOrder) {
        Class aClass = classRepository.findById(classId);

        if(aClass.isAvailable(userOrder.getStudentNumber()) && userOrder.getStudentNumber() <= 3) {
            Organization organization = aClass.getOrganization();
            userOrder.setUser(user);
            userOrder.setOrganization(organization);
            userOrder.setaClass(aClass);
            userOrder.setPaidToOrg(false);
            userOrder.setStatus(OrderStatus.WAITING_TO_BE_PAID);
            userOrder.setActualPrice(vipService.getDiscount(user.getId()) * userOrder.getStudentNumber() * aClass.getPrice());
            orderRepository.save(userOrder);
            return true;
        } else {
            return false;
        }
    }

    public boolean makeCourseReservation(User user, int courseId, UserOrder userOrder) {
        Course course = courseRepository.findById(courseId);

        if(userOrder.getStudentNumber() <= 9) {
            Organization organization = course.getOrganization();
            userOrder.setUser(user);
            userOrder.setOrganization(organization);
            userOrder.setPaidToOrg(false);
            userOrder.setStatus(OrderStatus.WAITING_TO_BE_PAID);

            // TODO Allocate classes 2 weeks before classes begin. And then calculate price.

            orderRepository.save(userOrder);
            return true;
        } else {
            return false;
        }
    }
}
