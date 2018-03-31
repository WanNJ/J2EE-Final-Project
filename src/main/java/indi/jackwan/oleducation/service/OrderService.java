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
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

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
    @Autowired
    private OrgService orgService;

    @Resource
    private ThreadPoolTaskScheduler threadPoolTaskScheduler;

    private ScheduledFuture<?> future;

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
        User user = userOrder.getUser();

        if (classList == null)
            return false;

        for (Class aClass: classList) {
            if (aClass.isAvailable(userOrder.getStudentNumber())) {
                aClass.setCurrentStudentNumber(aClass.getCurrentStudentNumber() + userOrder.getStudentNumber());
                userOrder.setaClass(aClass);
                userOrder.setActualPrice(vipService.getDiscount(userOrder.getUser().getId()) * userOrder.getStudentNumber() * aClass.getPrice());
                userOrder.setStatus(OrderStatus.WAITING_TO_BE_PAID);

                // Add user to class.
                user.getClassList().add(aClass);
                aClass.getUserList().add(user);

                classRepository.save(aClass);
                userService.save(user);

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

            // Add user to class.
            user.getClassList().add(aClass);
            aClass.getUserList().add(user);

            classRepository.save(aClass);
            userService.save(user);

            userOrder.setUser(user);
            userOrder.setOrganization(organization);
            userOrder.setCourse(aClass.getCourse());
            userOrder.setaClass(aClass);
            userOrder.setPaidToOrg(false);
            userOrder.setStatus(OrderStatus.WAITING_TO_BE_PAID);
            userOrder.setActualPrice(vipService.getDiscount(user.getId()) * userOrder.getStudentNumber() * aClass.getPrice());

            orderRepository.save(userOrder);

            invalidateOrderIn15Mins(userOrder.getId());

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
                invalidateOrderIn15Mins(userOrder.getId());
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
            user.addScore(userOrder.getActualPrice());
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

    /*
     * Cancel Order Logic.
     */
    public boolean cancelOrder(UserOrder userOrder) {
        if (userOrder.getStatus() != OrderStatus.PAID)
            return false;

        Date current = new Date();
        Date oneMonthFlag = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(oneMonthFlag);
        c.add(Calendar.MONTH, 1);
        oneMonthFlag = c.getTime();

        BankAccount account = userOrder.getBankAccount();
        Date beginDate = userOrder.getCourse().getStartTime();

        User user = userOrder.getUser();
        Class aClass = userOrder.getaClass();

        aClass.setCurrentStudentNumber(aClass.getCurrentStudentNumber() - userOrder.getStudentNumber());

        // Remove user from class.
        user.getClassList().remove(aClass);
        aClass.getUserList().remove(user);

        user.setExpenditure(user.getExpenditure() - userOrder.getActualPrice());
        user.setScore(user.getScore() - userOrder.getActualPrice());
        userService.save(user);
        classRepository.save(aClass);

        if (oneMonthFlag.before(beginDate)) {
            paymentService.transfer(rootBankAccount, account.getAccountAddress(), userOrder.getActualPrice());
            userOrder.setStatus(OrderStatus.CANCELLED);
        } else {
            if (current.before(beginDate)) {
                paymentService.transfer(rootBankAccount, account.getAccountAddress(), userOrder.getActualPrice() * 0.5);
                userOrder.setStatus(OrderStatus.CANCELLED);
            } else {
                userOrder.setStatus(OrderStatus.CANCELLED);
            }
        }

        orderRepository.save(userOrder);
        return true;
    }

    public void setAllOrdersPaidToOrgByOrgId(int orgId) {
        Organization organization = orgService.findByOrgId(orgId);
        List<UserOrder> userOrders = orderRepository.findUserOrdersByOrganization(organization);

        for (UserOrder order : userOrders) {
            if (!order.isPaidToOrg() && order.getStatus() == OrderStatus.PAID) {
                order.setPaidToOrg(true);
                orderRepository.save(order);
            }
        }
    }

    private void invalidateOrderIn15Mins(int orderId) {
        UserOrder order = orderRepository.findById(orderId);
        if (order != null) {
            Calendar cal = Calendar.getInstance();
            // Set delay.
            cal.add(Calendar.MINUTE, 15);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DATE);
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            int second = cal.get(Calendar.SECOND);
            String corn = second + " " + minute + " " + hour + " " + day + " " + month + " ?";
            future = threadPoolTaskScheduler.schedule(new InvalidateOrderListener(order), new CronTrigger(corn));
        }
    }

    private class InvalidateOrderListener implements Runnable {
        private UserOrder order;

        InvalidateOrderListener(UserOrder order) {
            this.order = order;
        }

        @Override
        public void run() {
            User user = order.getUser();
            order.setStatus(OrderStatus.EXPIRED);

            Class aClass =  order.getaClass();
            aClass.setCurrentStudentNumber(aClass.getCurrentStudentNumber() - order.getStudentNumber());

            // Remove user from class.
            user.getClassList().remove(aClass);
            aClass.getUserList().remove(user);

            userService.save(user);
            orderRepository.save(order);
            classRepository.save(aClass);

            // Close timer.
            if (future != null) {
                future.cancel(true);
            }
        }
    }
}
