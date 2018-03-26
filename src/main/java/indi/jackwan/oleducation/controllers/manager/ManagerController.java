package indi.jackwan.oleducation.controllers.manager;

import indi.jackwan.oleducation.models.OrgInfoChangeApplication;
import indi.jackwan.oleducation.models.Organization;
import indi.jackwan.oleducation.service.EmailService;
import indi.jackwan.oleducation.service.ManagerService;
import indi.jackwan.oleducation.service.OrgInfoChangeApplicationService;
import indi.jackwan.oleducation.service.OrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class ManagerController {
    @Autowired
    private OrgService orgService;
    @Autowired
    private ManagerService managerService;
    @Autowired
    private OrgInfoChangeApplicationService orgInfoChangeApplicationService;
    @Autowired
    private EmailService emailService;

    @RequestMapping(value = "/manager", method = RequestMethod.GET)
    public String showRegistrationPage(Model model, HttpSession session) {
        model.addAttribute("manager", session.getAttribute("manager"));

        // Organization Applications
        model.addAttribute("orgApplications", orgService.getUncheckedOrgApplications());

        // Organization Info Change Applications
        model.addAttribute("orgInfoChangeApplications", orgInfoChangeApplicationService.getUncheckedApplications());
        return "manager/manager";
    }

    @RequestMapping(value = "/manager/org/applications/approve/{orgCode}", method = RequestMethod.POST)
    public String approveApplication(Model model, HttpSession session, @PathVariable(value="orgCode") final String orgCode, RedirectAttributes redir) {
        Organization organization = orgService.findByOrgCode(orgCode);
        if(organization == null) {
            redir.addFlashAttribute("errorMessage", "No such application!");
            return "redirect:/manager";
        } else {
            organization.setDeclined(false);
            organization.setEnabled(true);
            orgService.save(organization);
            emailService.approveApplication(organization);
            redir.addFlashAttribute("successMessage", "You just approved organization(" + orgCode + ") successfully!" +
                    "An E-mail has been sent to the organization's mail box!");
            return "redirect:/manager";
        }
    }

    @RequestMapping(value = "/manager/org/applications/decline/{orgCode}", method = RequestMethod.POST)
    public String declineApplication(Model model, HttpSession session, @PathVariable(value="orgCode") final String orgCode, RedirectAttributes redir) {
        Organization organization = orgService.findByOrgCode(orgCode);
        if(organization == null) {
            redir.addFlashAttribute("errorMessage", "No such application!");
            return "redirect:/manager";
        } else {
            organization.setDeclined(true);
            organization.setEnabled(false);
            orgService.save(organization);
            emailService.declineApplication(organization);
            redir.addFlashAttribute("successMessage", "You just declined organization(" + orgCode + ") successfully!" +
                    "An E-mail has been sent to the organization's mail box!");
            return "redirect:/manager";
        }
    }

    @RequestMapping(value = "/manager/org/changeInfoApplications/approve/{id}", method = RequestMethod.POST)
    public String approveInfoChange(Model model, HttpSession session, @PathVariable(value="id") final int id, RedirectAttributes redir) {
        OrgInfoChangeApplication application = orgInfoChangeApplicationService.findById(id) ;
        if(application == null) {
            redir.addFlashAttribute("errorMessage", "No such application!");
            return "redirect:/manager";
        } else {
            Organization organization = orgService.findByOrgCode(application.getOrgCode());
            application.setDeclined(false);
            application.setApproved(true);
            orgInfoChangeApplicationService.save(application);
            orgService.updateByInfoChangeApplication(application);

            emailService.approveInfoChange(organization);

            redir.addFlashAttribute("successMessage", "You just approved organization(" + application.getOrgCode() + ")'s change on its information successfully!" +
                    "An E-mail has been sent to the organization's mail box!");
            return "redirect:/manager";
        }
    }

    @RequestMapping(value = "/manager/org/changeInfoApplications/decline/{id}", method = RequestMethod.POST)
    public String declineInfoChange(Model model, HttpSession session, @PathVariable(value="id") final int id, RedirectAttributes redir) {
        OrgInfoChangeApplication application = orgInfoChangeApplicationService.findById(id) ;
        if(application == null) {
            redir.addFlashAttribute("errorMessage", "No such application!");
            return "redirect:/manager";
        } else {
            Organization organization = orgService.findByOrgCode(application.getOrgCode());
            application.setDeclined(true);
            application.setApproved(false);
            orgInfoChangeApplicationService.save(application);
            orgService.updateByInfoChangeApplication(application);

            emailService.declineInfoChange(organization);

            redir.addFlashAttribute("successMessage", "You just declined organization(" + application.getOrgCode() + ")'s change on its information successfully!" +
                    "An E-mail has been sent to the organization's mail box!");
            return "redirect:/manager";
        }
    }
}
