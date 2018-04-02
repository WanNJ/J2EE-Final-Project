package indi.jackwan.oleducation.service;

import indi.jackwan.oleducation.models.BankAccount;
import indi.jackwan.oleducation.models.Manager;
import indi.jackwan.oleducation.models.UserOrder;
import indi.jackwan.oleducation.repositories.BankAccountRepository;
import indi.jackwan.oleducation.repositories.ManagerRepository;
import indi.jackwan.oleducation.utils.Enums.LoginResult;
import indi.jackwan.oleducation.utils.Enums.OrderStatus;
import indi.jackwan.oleducation.utils.Login.LoginUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("managerService")
public class ManagerService {
    @Value("${app.bankaccount.root}")
    private String rootBankAccountAddress;

    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private EmailService emailService;
    @Autowired
    private OrgService orgService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private BankAccountRepository bankAccountRepository;

    /**
     * Log a manager in.
     *
     * @param username    User inputed orgCode.
     * @param rawPassword User inputed raw password.
     * @return LoginResult
     */
    public LoginResult login(String username, String rawPassword) {
        Manager manager = managerRepository.findByUsername(username);
        if (manager == null)
            return LoginResult.NO_SUCH_ACCOUNT;
        System.out.println(bCryptPasswordEncoder.encode(rawPassword));
        boolean match = bCryptPasswordEncoder.matches(rawPassword, manager.getPassword());

        // Manager is always enabled.
        return LoginUtil.getLoginResult(match, true);
    }

    public Manager findByUsername(String username) {
        return managerRepository.findByUsername(username);
    }

    public boolean payToOrg(String bankAccount, int orgId) {
        BankAccount account = bankAccountRepository.findByAccountAddress(bankAccount);
        if (account == null)
            return false;

        double unpaidAmount = orgService.getUnpaidAmountByOrgId(orgId);
        BankAccount rootBankAccount = bankAccountRepository.findByAccountAddress(rootBankAccountAddress);

        if(paymentService.transfer(rootBankAccountAddress, bankAccount, unpaidAmount)) {
            orderService.setAllOrdersPaidToOrgByOrgId(orgId);
            return true;
        } else {
            return false;
        }
    }

    // TODO Cancelled amount.
    public double[] getFinancialStatisticData(){
        double[] result = {0,0,0,0};

        List<UserOrder> userOrders = orderService.findUserOrdersByStatus(OrderStatus.PAID);

        for (UserOrder order: userOrders) {
            result[0] += order.getActualPrice();
            if(!order.isPaidToOrg()) {
                result[1] += order.getActualPrice();
            }
        }

        // Organization only receives 80%.
        result[1] = result[1] * 0.8;

        // Platform got 20% net profit.
        result[2] = result[0] * 0.2;
        result[3] = result[2] + result[1];

        return result;
    }
}
