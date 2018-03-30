package indi.jackwan.oleducation.controllers.org;

import indi.jackwan.oleducation.models.BankAccount;
import indi.jackwan.oleducation.models.UserOrder;
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
        return "redirect:/org/enrollment";
    }
}
