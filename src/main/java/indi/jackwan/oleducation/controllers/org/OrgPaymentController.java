package indi.jackwan.oleducation.controllers.org;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class OrgPaymentController {
    @RequestMapping(value = "/org/payment", method = RequestMethod.GET)
    public String showPaymentPage(Model model, HttpSession session) {
        model.addAttribute("org", session.getAttribute("org"));
        return "org/payment";
    }
}
