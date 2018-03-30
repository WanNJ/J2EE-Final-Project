package indi.jackwan.oleducation.service;

import indi.jackwan.oleducation.models.BankAccount;
import indi.jackwan.oleducation.models.User;
import indi.jackwan.oleducation.models.UserOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("orgCourseService")
public class OrgCourseService {
    @Autowired
    private OrderService orderService;

    public boolean enrollUserIntoClass(User user, int classId, UserOrder userOrder, BankAccount account) {
        return orderService.makeClassReservation(user, classId, userOrder) && orderService.payForOrder(user, userOrder, account);
    }
}
