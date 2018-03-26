package indi.jackwan.oleducation.controllers.org;

import indi.jackwan.oleducation.models.Course;
import indi.jackwan.oleducation.models.OrgInfoChangeApplication;
import indi.jackwan.oleducation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class OrgController {
    @Autowired
    private UserService userService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("MM/dd/yyyy"), true));
    }

    @RequestMapping(value = "/org", method = RequestMethod.GET)
    public String showRegistrationPage(Model model, HttpSession session, @ModelAttribute(value = "application") OrgInfoChangeApplication application,
                                       @ModelAttribute(value = "course") Course course) {
        // Overall View
        model.addAttribute("org", session.getAttribute("org"));

        return "org/org";
    }

    @RequestMapping(value = "/org/release-course", method = RequestMethod.POST)
    public String processCourseRelease(Model model, HttpSession session, @ModelAttribute(value = "course") Course course) {

        return "redirect:/org";
    }
}