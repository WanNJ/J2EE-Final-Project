package indi.jackwan.oleducation.service;

import indi.jackwan.oleducation.models.OrgInfoChangeApplication;
import indi.jackwan.oleducation.models.Organization;
import indi.jackwan.oleducation.repositories.OrgInfoChangeApplicationRepository;
import indi.jackwan.oleducation.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("orgInfoChangeApplicationService")
public class OrgInfoChangeApplicationService {
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private OrgInfoChangeApplicationRepository orgInfoChangeApplicationRepository;
    @Autowired
    private EmailService emailService;

    public void approve(OrgInfoChangeApplication application) {
        application.setApproved(true);
        application.setDeclined(false);

        Organization org = organizationRepository.findByOrgCode(application.getOrgCode());
        org.setName(application.getName());
        org.setLocation(application.getLocation());
        org.setTeacherNum(application.getTeacherNum());

        organizationRepository.save(org);
        orgInfoChangeApplicationRepository.save(application);
    }

    public void decline(OrgInfoChangeApplication application) {
        application.setApproved(false);
        application.setDeclined(true);
        orgInfoChangeApplicationRepository.save(application);
    }

    public List<OrgInfoChangeApplication> getUncheckedApplications() {
        return orgInfoChangeApplicationRepository.findByDeclinedAndApproved(false, false);
    }

    public List<OrgInfoChangeApplication> getApprovedApplications() {
        return orgInfoChangeApplicationRepository.findByDeclinedAndApproved(false, true);
    }

    public List<OrgInfoChangeApplication> getDeclinedApplications() {
        return orgInfoChangeApplicationRepository.findByDeclinedAndApproved(true, false);
    }

    public void save(OrgInfoChangeApplication application) {
        orgInfoChangeApplicationRepository.save(application);
    }
}
