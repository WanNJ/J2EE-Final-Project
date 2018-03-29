package indi.jackwan.oleducation.service;

import indi.jackwan.oleducation.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Discount strategies are defined here.
 */
@Service("vipService")
public class VipService {
    @Autowired
    private UserService userService;

    public double getDiscount(int userId) {
        User user = userService.findById(userId);
        double expenditure = user.getExpenditure();
        if (expenditure < 100) {
            return 1;
        }
        else if (100 <= expenditure && expenditure < 300) {
            return 0.9;
        }
        else if (300 <= expenditure && expenditure < 500) {
            return 0.8;
        }
        else {
            return 0.6;
        }
    }
}
