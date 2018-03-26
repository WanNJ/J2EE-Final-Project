package indi.jackwan.oleducation.controllers.org;

import indi.jackwan.oleducation.models.OrgInfoChangeApplication;
import indi.jackwan.oleducation.models.Organization;
import indi.jackwan.oleducation.service.OrgInfoChangeApplicationService;
import indi.jackwan.oleducation.service.OrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class OrgInfoController {
    @Autowired
    private OrgService orgService;
    @Autowired
    private OrgInfoChangeApplicationService orgInfoChangeApplicationService;

    @RequestMapping(value = "/org/info/changeInfo", method = RequestMethod.POST)
    public String changeOrgInfo(Model model, @ModelAttribute(value = "application") OrgInfoChangeApplication application, HttpSession session, RedirectAttributes redir) {
        if(application.getName().equals("")) {
            redir.addFlashAttribute("dangerMessage", "Name cannot be empty!");
        } else if (application.getLocation().equals("")) {
            redir.addFlashAttribute("dangerMessage", "Location cannot be empty!");
        } else if (application.getTeacherNum() < 0) {
            redir.addFlashAttribute("dangerMessage", "Teacher number cannot be negative!");
        } else {
            Organization currentOrganization = (Organization) session.getAttribute("org");
            application.setOrgCode(currentOrganization.getOrgCode());
            orgInfoChangeApplicationService.save(application);
            redir.addFlashAttribute("successMessage", "You've just submitted your application of changing the information of your organization!");
        }
        return "redirect:/org";
    }
}