package indi.jackwan.oleducation.controllers.user;

import indi.jackwan.oleducation.models.User;
import indi.jackwan.oleducation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class UserMembershipController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user/membership", method = RequestMethod.GET)
    public String getMembershipPage(Model model, HttpSession session, RedirectAttributes redir) {
        model.addAttribute("user", session.getAttribute("user"));
        return "user/membership";
    }

    @RequestMapping(value = "/user/cancelMembership", method = RequestMethod.POST)
    public String cancelMembership(Model model, HttpSession session, RedirectAttributes redir) {
        User currentUser = (User) session.getAttribute("user");
        currentUser.setVip(false);
        userService.save(currentUser);
        redir.addFlashAttribute("successMessage", "You just cancelled your membership!");
        return "redirect:/user/cancelMembership";
    }
}
