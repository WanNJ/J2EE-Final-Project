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

    @RequestMapping(value = "/org/grades/signin", method = RequestMethod.POST)
    public String signInStudent(Model model, HttpSession session, @RequestParam("classId") int classId,
                                @RequestParam("email") String email, RedirectAttributes redir) {
        Organization organization = (Organization) session.getAttribute("org");
        SignInStudentResult result = orgClassService.signInStudent(classId, email, organization);

        return "redirect:/org/grades";
    }
}