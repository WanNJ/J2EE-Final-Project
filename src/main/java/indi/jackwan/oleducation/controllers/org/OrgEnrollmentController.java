package indi.jackwan.oleducation.controllers.org;

import indi.jackwan.oleducation.models.BankAccount;
import indi.jackwan.oleducation.models.Class;
import indi.jackwan.oleducation.models.User;
import indi.jackwan.oleducation.models.UserOrder;
import indi.jackwan.oleducation.repositories.ClassRepository;
import indi.jackwan.oleducation.service.OrgCourseService;
import indi.jackwan.oleducation.service.PaymentService;
import indi.jackwan.oleducation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class OrgEnrollmentController {
    @Autowired
    private OrgCourseService orgCourseService;
    @Autowired
    private UserService userService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ClassRepository classRepository;

    @RequestMapping(value = "/org/enrollment", method = RequestMethod.GET)
    public String showPaymentPage(Model model, HttpSession session, @ModelAttribute(value = "bankAccount") BankAccount bankAccount,
                                  @ModelAttribute(value = "userOrder") UserOrder userOrder) {
        model.addAttribute("org", session.getAttribute("org"));
        return "org/enrollment";
    }

    @RequestMapping(value = "/org/enrollment", method = RequestMethod.POST)
    public String orgEnrollClass(Model model, HttpSession session, @RequestParam("classId") int classId,
                                 @RequestParam("email") String email, @ModelAttribute(value = "bankAccount") BankAccount bankAccount,
                                 @ModelAttribute(value = "userOrder") UserOrder userOrder, RedirectAttributes redir) {
        User user = userService.findByEmail(email);
        if (user == null) {
            redir.addFlashAttribute("errorMessage", "No such user!");
        }

        Class aClass = classRepository.findById(classId);
        if (aClass == null) {
            redir.addFlashAttribute("errorMessage", "No such class!");
        }

        if (!paymentService.validate(bankAccount)) {
            redir.addFlashAttribute("errorMessage", "Wrong bank account inforamtion!");
        } else {
            if (orgCourseService.enrollUserIntoClass(user, classId, userOrder, bankAccount)) {
                redir.addFlashAttribute("successMessage", "You have just reserved an order succesfully!");
            }
        }

        return "redirect:/org/enrollment";
    }
}
