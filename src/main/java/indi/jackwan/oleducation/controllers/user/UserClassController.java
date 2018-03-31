package indi.jackwan.oleducation.controllers.user;

import indi.jackwan.oleducation.models.ClassSignIn;
import indi.jackwan.oleducation.models.Grade;
import indi.jackwan.oleducation.models.User;
import indi.jackwan.oleducation.service.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserClassController {
    @Autowired
    private ClassService classService;


    @RequestMapping(value = "/user/class/{classId}", method = RequestMethod.GET)
    public String getCourseClasses(Model model, @PathVariable(value = "classId") final int classId,
                                   HttpSession session, RedirectAttributes redir) {
        User user = (User) session.getAttribute("user");
        List<ClassSignIn> signInList = classService.getSignInListByUserAndClass(user, classId);
        List<Grade> gradeList = classService.getGradesListByUserAndClass(user, classId);

        model.addAttribute("user", user);
        model.addAttribute("signInList", signInList);
        model.addAttribute("gradeList", gradeList);

        return "user/class-details";
    }
}
