package indi.jackwan.oleducation.controllers.manager;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class ManagerOrganizationStatisticsController {
    @RequestMapping(value = "/manager/organization-statistics", method = RequestMethod.GET)
    public String showOrgStatisticsPage(Model model, HttpSession session) {
        model.addAttribute("manager", session.getAttribute("manager"));
        return "manager/org-statistics";
    }
}
