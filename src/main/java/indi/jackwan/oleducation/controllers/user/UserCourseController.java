package indi.jackwan.oleducation.controllers.user;

import indi.jackwan.oleducation.models.Class;
import indi.jackwan.oleducation.models.Course;
import indi.jackwan.oleducation.models.UserOrder;
import indi.jackwan.oleducation.service.CourseService;
import indi.jackwan.oleducation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserCourseController {
    @Autowired
    private UserService userService;
    @Autowired
    private CourseService courseService;

    @RequestMapping(value = "/user/courses", method = RequestMethod.GET)
    public String getCoursePage(Model model, HttpSession session, RedirectAttributes redir) {
        model.addAttribute("user", session.getAttribute("user"));

        List<Course> courseList = courseService.getAllCourses();
        model.addAttribute("courseList", courseList);

        return "user/course-market";
    }

    @RequestMapping(value = "/user/course/{id}/classes", method = RequestMethod.GET)
    public String getCourseClasses(Model model, @PathVariable(value = "id") final int courseId, HttpSession session, RedirectAttributes redir) {
        model.addAttribute("user", session.getAttribute("user"));

        Course course = courseService.findById(courseId);
        List<Class> classList = courseService.getAllClass(course);
        model.addAttribute("currentCourse", course);
        model.addAttribute("classList", classList);

        return "user/course-class";
    }

    @RequestMapping(value = "/user/course/{courseId}/class/{classId}/enroll", method = RequestMethod.GET)
    public String enrollClass(Model model, @PathVariable(value = "courseId") final int courseId,
                              @PathVariable(value = "classId") final int classId, @ModelAttribute(value = "userOrder") UserOrder userOrder,
                              HttpSession session, RedirectAttributes redir) {
        model.addAttribute("user", session.getAttribute("user"));

        Course course = courseService.findById(courseId);
        Class aClass = courseService.getClassById(classId);

        model.addAttribute("course", course);
        model.addAttribute("class", aClass);

        return "user/enroll-class-with-reservation";
    }

    @RequestMapping(value = "/user/course/{courseId}/enroll", method = RequestMethod.GET)
    public String enrollCourse(Model model, @PathVariable(value = "courseId") final int courseId, @ModelAttribute(value = "userOrder") UserOrder userOrder,
                               HttpSession session, RedirectAttributes redir) {
        model.addAttribute("user", session.getAttribute("user"));

        Course course = courseService.findById(courseId);
        model.addAttribute("course", course);

        return "user/enroll-course-without-reservation";
    }
}
