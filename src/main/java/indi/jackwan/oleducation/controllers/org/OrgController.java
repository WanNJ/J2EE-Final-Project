package indi.jackwan.oleducation.controllers.org;

import indi.jackwan.oleducation.models.Course;
import indi.jackwan.oleducation.models.OrgInfoChangeApplication;
import indi.jackwan.oleducation.models.Organization;
import indi.jackwan.oleducation.service.CourseService;
import indi.jackwan.oleducation.utils.Enums.ReleaseCourseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class OrgController {
    @Autowired
    private CourseService courseService;

    // Make sure that "startTime" and "endTime" can be parsed properly.
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
    public String processCourseRelease(Model model, HttpSession session, @ModelAttribute(value = "course") Course course, RedirectAttributes redir) {
        course.setOrganization((Organization) session.getAttribute("org"));
        ReleaseCourseResult releaseCourseResult = courseService.releaseCourse(course);

        if (releaseCourseResult == ReleaseCourseResult.NO_SUCH_ORGANIZATION) {
            redir.addFlashAttribute("errorMessage", "Please logout and login again! There's something wrong with your cookie.");
        } else if (releaseCourseResult == ReleaseCourseResult.INVALID_TIME) {
            redir.addFlashAttribute("errorMessage", "Invalid time period!");
        } else {
            redir.addFlashAttribute("successMessage", "You just released a course successfully!");
        }

        return "redirect:/org";
    }
}