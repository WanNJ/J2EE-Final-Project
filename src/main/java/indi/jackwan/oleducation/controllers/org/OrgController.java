package indi.jackwan.oleducation.controllers.org;

import indi.jackwan.oleducation.models.Course;
import indi.jackwan.oleducation.models.Organization;
import indi.jackwan.oleducation.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    public String showOrgPage(Model model, HttpSession session) {
        Organization organization = (Organization) session.getAttribute("org");

        // Overall View
        model.addAttribute("org", organization);

        // Org Courses
        List<Course> courseList = courseService.findCoursesByOrganization(organization);
        model.addAttribute("courseList", courseList);

        return "org/dashboard";
    }
}