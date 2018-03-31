package indi.jackwan.oleducation.controllers.manager;

import indi.jackwan.oleducation.models.Organization;
import indi.jackwan.oleducation.service.ManagerService;
import indi.jackwan.oleducation.service.OrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ManagerOrganizationController {
    @Autowired
    private OrgService orgService;
    @Autowired
    private ManagerService managerService;

    @RequestMapping(value = "/manager/organizations", method = RequestMethod.GET)
    public String showOrganizationsPage(Model model, HttpSession session) {
        model.addAttribute("manager", session.getAttribute("manager"));
        List<Organization> organizationList = orgService.getAllOrgs();

        model.addAttribute("organizationList", organizationList);

        return "manager/orgs";
    }

    @RequestMapping(value = "/manager/org/{orgId}", method = RequestMethod.GET)
    public String showOrganizationPage(Model model, HttpSession session, @PathVariable(value = "orgId") final int orgId,
                                       RedirectAttributes redir) {
        model.addAttribute("manager", session.getAttribute("manager"));
        Organization organization = orgService.findByOrgId(orgId);

        if (organization == null) {
            redir.addFlashAttribute("errorMessage", "No such organization!");
            return "redirect:/manager/organizations";
        }

        double unpaidAmount = orgService.getUnpaidAmountByOrgId(orgId);

        model.addAttribute("organization", organization);
        model.addAttribute("unpaidAmount", unpaidAmount);

        return "manager/organization";
    }

    @RequestMapping(value = "/manager/org/{orgId}/pay", method = RequestMethod.POST)
    public String payToOrg(Model model, HttpSession session, @PathVariable(value = "orgId") final int orgId,
                           @RequestParam("bankAccount") String bankAccount, RedirectAttributes redir) {
        if (managerService.payToOrg(bankAccount, orgId)) {
            redir.addFlashAttribute("successMessage", "Payment to organization succeeded!");
        } else {
            redir.addFlashAttribute("errorMessage", "Oops, something went wrong!");
        }

        return "redirect:/manager/org/" + orgId;
    }
}
