package indi.jackwan.oleducation.controllers.manager;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class ManagerController {
    @RequestMapping(value = "/manager", method = RequestMethod.GET)
    public String showManagerPage(Model model, HttpSession session) {
        model.addAttribute("manager", session.getAttribute("manager"));
        return "manager/dashboard";
    }
}
