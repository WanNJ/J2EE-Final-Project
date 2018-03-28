package indi.jackwan.oleducation.controllers.org;

import indi.jackwan.oleducation.models.Class;
import indi.jackwan.oleducation.models.Course;
import indi.jackwan.oleducation.models.Organization;
import indi.jackwan.oleducation.service.CourseService;
import indi.jackwan.oleducation.utils.Enums.ReleaseCourseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class OrgCourseController {
    @Autowired
    private CourseService courseService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("MM/dd/yyyy"), true));
    }

    @RequestMapping(value = "/org/course", method = RequestMethod.GET)
    public String showCoursePage(Model model, HttpSession session, @ModelAttribute(value = "course") Course course) {
        model.addAttribute("org", session.getAttribute("org"));
        return "org/release-course";
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

        return "redirect:/org/course";
    }

    @RequestMapping(value = "/org/course/{id}/add-class", method = RequestMethod.GET)
    public String showClassPage(Model model, HttpSession session, @PathVariable(value = "id") final int courseId, @ModelAttribute(value = "class") Class aClass) {
        model.addAttribute("org", session.getAttribute("org"));

        Course currentCourse = courseService.findById(courseId);

        model.addAttribute("currentCourse", currentCourse);
        model.addAttribute("classList", courseService.getAllClass(currentCourse));

        return "org/add-class";
    }

    @RequestMapping(value = "/org/course/{id}/add-class", method = RequestMethod.POST)
    public String showCoursePage(Model model, HttpSession session, @PathVariable(value = "id") final int courseId,
                                 @ModelAttribute(value = "class") Class aClass, RedirectAttributes redir) {
        Organization organization = (Organization) session.getAttribute("org");
        Course currentCourse = courseService.findById(courseId);

        if (courseService.addClass(organization, currentCourse, aClass))
            redir.addFlashAttribute("successMessage", "You've just added a class to this course successfully!");
        else
            redir.addFlashAttribute("errorMessage", "You've just inputed some invalid value. Try again!");
        return "redirect:/org/course/" +  String.valueOf(courseId) + "/add-class";
    }
}
