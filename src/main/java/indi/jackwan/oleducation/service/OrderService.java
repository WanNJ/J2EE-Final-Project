package indi.jackwan.oleducation.service;

import indi.jackwan.oleducation.models.*;
import indi.jackwan.oleducation.models.Class;
import indi.jackwan.oleducation.repositories.BankAccountRepository;
import indi.jackwan.oleducation.repositories.ClassRepository;
import indi.jackwan.oleducation.repositories.CourseRepository;
import indi.jackwan.oleducation.repositories.OrderRepository;
import indi.jackwan.oleducation.utils.Enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("orderService")
public class OrderService {
    @Value("${app.bankaccount.root}")
    private String rootBankAccount;

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
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private BankAccountRepository bankAccountRepository;

    public UserOrder findById(int id) {
        return orderRepository.findById(id);
    }

    public List<UserOrder> findUserOrdersByOrganization(Organization organization) {
        return orderRepository.findUserOrdersByOrganization(organization);
    }

    public List<UserOrder> findUserOrdersByUser(User user) {
        return orderRepository.findUserOrdersByUser(user);
    }

    private boolean autoAllocateOrder(UserOrder userOrder, Course course) {
        List<Class> classList = classRepository.findClassesByCourse(course);

        if (classList == null)
            return false;

        for (Class aClass: classList) {
            if (aClass.isAvailable(userOrder.getStudentNumber())) {
                aClass.setCurrentStudentNumber(aClass.getCurrentStudentNumber() + userOrder.getStudentNumber());
                userOrder.setaClass(aClass);
                userOrder.setActualPrice(vipService.getDiscount(userOrder.getUser().getId()) * userOrder.getStudentNumber() * aClass.getPrice());
                userOrder.setStatus(OrderStatus.WAITING_TO_BE_PAID);
                classRepository.save(aClass);
                orderRepository.save(userOrder);
                return true;
            }
        }

        return false;
    }

    public boolean makeClassReservation(User user, int classId, UserOrder userOrder) {
        Class aClass = classRepository.findById(classId);

        if(aClass.isAvailable(userOrder.getStudentNumber()) && userOrder.getStudentNumber() <= 3) {
            Organization organization = aClass.getOrganization();

            // Update current studentNumber
            aClass.setCurrentStudentNumber(aClass.getCurrentStudentNumber() + userOrder.getStudentNumber());
            classRepository.save(aClass);

            userOrder.setUser(user);
            userOrder.setOrganization(organization);
            userOrder.setCourse(aClass.getCourse());
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

    public OrderStatus makeCourseReservation(User user, int courseId, UserOrder userOrder) {
        Course course = courseRepository.findById(courseId);

        if(userOrder.getStudentNumber() <= 9) {
            Organization organization = course.getOrganization();
            userOrder.setUser(user);
            userOrder.setOrganization(organization);
            userOrder.setCourse(course);
            userOrder.setPaidToOrg(false);

            if (autoAllocateOrder(userOrder, course)) {
                return OrderStatus.WAITING_TO_BE_PAID;
            }
            else {
                userOrder.setStatus(OrderStatus.UNSUCCESSFULL);
                orderRepository.save(userOrder);
                return OrderStatus.UNSUCCESSFULL;
            }

        } else {
            return OrderStatus.INVALID;
        }
    }

    public boolean payForOrder(User user, UserOrder userOrder, BankAccount bankAccount) {
        if (userOrder.getStatus() != OrderStatus.WAITING_TO_BE_PAID)
            return false;

        if (paymentService.transfer(bankAccount.getAccountAddress(), rootBankAccount, userOrder.getActualPrice())) {
            user.addExpenditure(userOrder.getActualPrice());
            userService.save(user);

            BankAccount account = bankAccountRepository.findByAccountAddress(bankAccount.getAccountAddress());

            userOrder.setBankAccount(account);
            userOrder.setStatus(OrderStatus.PAID);
            orderRepository.save(userOrder);
            return true;
        } else {
            return false;
        }

    }
}
