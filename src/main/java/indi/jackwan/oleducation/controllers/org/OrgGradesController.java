package indi.jackwan.oleducation.controllers.org;

import indi.jackwan.oleducation.models.Organization;
import indi.jackwan.oleducation.service.ClassService;
import indi.jackwan.oleducation.utils.Enums.SignInStudentResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class OrgGradesController {
    @Autowired
    private ClassService orgClassService;

    @RequestMapping(value = "/org/grades", method = RequestMethod.GET)
    public String showGradesPage(Model model, HttpSession session) {
        model.addAttribute("org", session.getAttribute("org"));
        return "org/grades";
    }

    @SuppressWarnings("Duplicates")
    @RequestMapping(value = "/org/grades/signin", method = RequestMethod.POST)
    public String signInStudent(Model model, HttpSession session, @RequestParam("classId") int classId,
                                @RequestParam("email") String email, RedirectAttributes redir) {
        Organization organization = (Organization) session.getAttribute("org");
        SignInStudentResult result = orgClassService.signInStudent(classId, email, organization);

        if (result == SignInStudentResult.NO_SUCH_CLASS_OR_NO_ACCESS_TO_CLASS) {
            redir.addFlashAttribute("errorMessage", "There's no such class or you have no access to this class! Please check later.");
        }
        else if (result == SignInStudentResult.NO_SUCH_USER) {
            redir.addFlashAttribute("errorMessage", "There's no such user!");
        }
        else if (result == SignInStudentResult.USER_NOT_IN_CLASS) {
            redir.addFlashAttribute("errorMessage", "This user is not in this class currently, please check!");
        } else {
            redir.addFlashAttribute("successMessage", "You've just signed in user whose e-mail is " + email + " to class " + classId + " successfully!");
        }

        return "redirect:/org/grades";
    }

    @RequestMapping(value = "/org/grades/grade", method = RequestMethod.POST)
    public String recordGrades(Model model, HttpSession session, @RequestParam("classId") int classId,
                               @RequestParam("email") String email, @RequestParam("score") double score, RedirectAttributes redir) {
        Organization organization = (Organization) session.getAttribute("org");
        SignInStudentResult result = orgClassService.recordGrades(score, classId, email, organization);

        if (result == SignInStudentResult.NO_SUCH_CLASS_OR_NO_ACCESS_TO_CLASS) {
            redir.addFlashAttribute("errorMessage", "There's no such class or you have no access to this class! Please check later.");
        }
        else if (result == SignInStudentResult.NO_SUCH_USER) {
            redir.addFlashAttribute("errorMessage", "There's no such user!");
        }
        else if (result == SignInStudentResult.USER_NOT_IN_CLASS) {
            redir.addFlashAttribute("errorMessage", "This user is not in this class currently, please check!");
        } else {
            redir.addFlashAttribute("successMessage", "You've just added a new grade record to user whose e-mail is " + email + " to class " + classId + " successfully!");
        }

        return "redirect:/org/grades";
    }
}