package indi.jackwan.oleducation.controllers.org;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class OrgStatisticsController {

    @RequestMapping(value = "/org/order-statistics", method = RequestMethod.GET)
    public String showOrderStatisticsPage(Model model, HttpSession session) {
        model.addAttribute("org", session.getAttribute("org"));
        return "org/order-statistics";
    }

    @RequestMapping(value = "/org/financial-statistics", method = RequestMethod.GET)
    public String showFinancialStatisticsPage(Model model, HttpSession session) {
        model.addAttribute("org", session.getAttribute("org"));
        return "org/financial-statistics";
    }
}
