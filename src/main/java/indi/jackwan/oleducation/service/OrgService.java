package indi.jackwan.oleducation.service;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import indi.jackwan.oleducation.models.Organization;
import indi.jackwan.oleducation.repositories.OrganizationRepository;
import indi.jackwan.oleducation.utils.Enums.LoginResult;
import indi.jackwan.oleducation.utils.Enums.RegisterResult;
import indi.jackwan.oleducation.utils.Login.LoginUtil;
import indi.jackwan.oleducation.utils.Register.OrgRegister;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service("orgService")
public class OrgService {
    @Autowired
    private OrganizationRepository orgRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private EmailService emailService;

    /**
     * Log an user in.
     * @param orgCode       User inputed orgCode.
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
        if(org != null)
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

    public Organization findByOrgCode(String orgCode) {
        return orgRepository.findByOrgCode(orgCode);
    }

    public void save(Organization org) {
        orgRepository.save(org);
    }
}
