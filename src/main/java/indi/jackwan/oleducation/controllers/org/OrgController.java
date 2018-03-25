package indi.jackwan.oleducation.controllers.org;

import indi.jackwan.oleducation.models.OrgInfoChangeApplication;
import indi.jackwan.oleducation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class OrgController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/org", method = RequestMethod.GET)
    public String showRegistrationPage(Model model, HttpSession session, @ModelAttribute(value = "application") OrgInfoChangeApplication application) {
        model.addAttribute("org", session.getAttribute("org"));
        return "org/org";
    }
}