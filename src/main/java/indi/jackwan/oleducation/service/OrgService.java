package indi.jackwan.oleducation.service;

import indi.jackwan.oleducation.models.Organization;
import indi.jackwan.oleducation.repositories.OrganizationRepository;
import indi.jackwan.oleducation.utils.Enums.LoginResult;
import indi.jackwan.oleducation.utils.Login.LoginUtil;
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

    public Organization findByOrgCode(String orgCode) {
        return orgRepository.findByOrgCode(orgCode);
    }

    public void save(Organization org) {
        orgRepository.save(org);
    }
}
