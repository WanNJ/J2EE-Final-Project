package indi.jackwan.oleducation.service;

import indi.jackwan.oleducation.models.BankAccount;
import indi.jackwan.oleducation.models.Manager;
import indi.jackwan.oleducation.repositories.BankAccountRepository;
import indi.jackwan.oleducation.repositories.ManagerRepository;
import indi.jackwan.oleducation.utils.Enums.LoginResult;
import indi.jackwan.oleducation.utils.Login.LoginUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
}
