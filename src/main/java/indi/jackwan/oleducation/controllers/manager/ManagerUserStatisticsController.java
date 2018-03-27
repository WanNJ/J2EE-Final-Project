package indi.jackwan.oleducation.controllers.manager;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class ManagerUserStatisticsController {
    @RequestMapping(value = "/manager/user-statistics", method = RequestMethod.GET)
    public String showUserStatisticsPage(Model model, HttpSession session) {
        model.addAttribute("manager", session.getAttribute("manager"));
        return "manager/user-statistics";
    }
}
