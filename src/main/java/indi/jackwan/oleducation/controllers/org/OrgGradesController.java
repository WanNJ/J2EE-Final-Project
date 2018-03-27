package indi.jackwan.oleducation.controllers.org;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class OrgGradesController {
    @RequestMapping(value = "/org/grades", method = RequestMethod.GET)
    public String showGradesPage(Model model, HttpSession session) {
        model.addAttribute("org", session.getAttribute("org"));
        return "org/grades";
    }
}
