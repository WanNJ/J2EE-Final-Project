package indi.jackwan.oleducation.service;

import indi.jackwan.oleducation.models.BankAccount;
import indi.jackwan.oleducation.models.User;
import indi.jackwan.oleducation.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Discount strategies are defined here.
 */
@Service("vipService")
public class VipService {
    @Value("${app.bankaccount.root}")
    private String rootBankAccount;

    @Autowired
    private UserService userService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private BankAccountRepository bankAccountRepository;

    public double getDiscount(int userId) {
        User user = userService.findById(userId);
        double expenditure = user.getExpenditure();
        if (expenditure < 100) {
            return 1;
        } else if (100 <= expenditure && expenditure < 300) {
            return 0.9;
        } else if (300 <= expenditure && expenditure < 500) {
            return 0.8;
        } else {
            return 0.6;
        }
    }

    public boolean transferScore(User user, String bankAccount, double amount) {
        if (user.getScore() < amount) {
            return false;
        }

        BankAccount account = bankAccountRepository.findByAccountAddress(bankAccount);

        if (account == null)
            return false;
        else {
            if (paymentService.transfer(rootBankAccount, bankAccount, amount / 20)) {
                user.setScore(user.getScore() - amount);
                userService.save(user);
                return true;
            }
            return false;
        }
    }
}
