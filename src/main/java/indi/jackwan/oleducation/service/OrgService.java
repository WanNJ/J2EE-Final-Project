package indi.jackwan.oleducation.service;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import indi.jackwan.oleducation.models.Course;
import indi.jackwan.oleducation.models.OrgInfoChangeApplication;
import indi.jackwan.oleducation.models.Organization;
import indi.jackwan.oleducation.models.UserOrder;
import indi.jackwan.oleducation.repositories.OrderRepository;
import indi.jackwan.oleducation.repositories.OrganizationRepository;
import indi.jackwan.oleducation.utils.Enums.LoginResult;
import indi.jackwan.oleducation.utils.Enums.OrderStatus;
import indi.jackwan.oleducation.utils.Enums.RegisterResult;
import indi.jackwan.oleducation.utils.Enums.ReleaseCourseResult;
import indi.jackwan.oleducation.utils.Login.LoginUtil;
import indi.jackwan.oleducation.utils.Register.OrgRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("orgService")
public class OrgService {
    @Autowired
    private OrganizationRepository orgRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private EmailService emailService;
    @Autowired
    private OrderRepository orderRepository;

    /**
     * Log an user in.
     *
     * @param orgCode     User inputed orgCode.
     * @param rawPassword User inputed raw password.
     * @return LoginResult
     */
    public LoginResult login(String orgCode, String rawPassword) {
        Organization org = orgRepository.findByOrgCode(orgCode);
        if (org == null)
            return LoginResult.NO_SUCH_ACCOUNT;
        boolean match = bCryptPasswordEncoder.matches(rawPassword, org.getPassword());

        return LoginUtil.getLoginResult(match, org.isEnabled());
    }

    public RegisterResult register(Organization organization) throws Exception {
        Organization org = orgRepository.findByName(organization.getName());
        if (org != null)
            return RegisterResult.ALREADY_REGISTERED;

        Zxcvbn passwordCheck = new Zxcvbn();
        Strength strength = passwordCheck.measure(organization.getPassword());

        if (strength.getScore() < 3) {
            return RegisterResult.PASSWORD_TOO_WEAK;
        } else {
            organization.setPassword(bCryptPasswordEncoder.encode(organization.getPassword()));
            organization.setEnabled(false);
            organization.setDeclined(false);
            orgRepository.save(organization);
            organization = orgRepository.findByName(organization.getName());
            organization.setOrgCode(OrgRegister.generateOrgCodeFromId(organization.getId()));
            orgRepository.save(organization);
            emailService.sendOrgCode(organization);
            return RegisterResult.SUCCESS;
        }
    }

    public List<Organization> getUncheckedOrgApplications() {
        return orgRepository.findByDeclinedAndEnabled(false, false);
    }

    public void updateByInfoChangeApplication(OrgInfoChangeApplication application) {
        Organization organization = orgRepository.findByOrgCode(application.getOrgCode());
        organization.setName(application.getName());
        organization.setLocation(application.getLocation());
        organization.setTeacherNum(application.getTeacherNum());
        orgRepository.save(organization);
    }

    public ReleaseCourseResult releaseCourse(Course course) {
        return ReleaseCourseResult.SUCCESS;
    }

    public Organization findByOrgCode(String orgCode) {
        return orgRepository.findByOrgCode(orgCode);
    }

    public List<Organization> getAllOrgs() {
        return orgRepository.findAll();
    }

    public void save(Organization org) {
        orgRepository.save(org);
    }

    public Organization findByOrgId(int orgId) {
        return orgRepository.findById(orgId);
    }

    /**
     * Unpaid money logics.
     */
    public double getUnpaidAmountByOrgId(int orgId) {
        Organization organization = orgRepository.findById(orgId);
        List<UserOrder> userOrders = orderRepository.findUserOrdersByOrganization(organization);

        double unpaidAmount = 0;

        for (UserOrder order : userOrders) {
            if (!order.isPaidToOrg() && order.getStatus() == OrderStatus.PAID)
                unpaidAmount += order.getActualPrice();
        }

        return unpaidAmount * 0.8;
    }
}
