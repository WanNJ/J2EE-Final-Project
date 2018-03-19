package indi.jackwan.oleducation.controllers;

import indi.jackwan.oleducation.models.User;
import indi.jackwan.oleducation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class LoginController {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showRegistrationPage(Model model, User user, @RequestParam(value="message", required=false) String message) {
        if (message != null) {
            model.addAttribute("normalErrorMessage", message);
        }
        model.addAttribute("user", user);
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String processLoginForm(Model model, User user, HttpSession session, RedirectAttributes redir) {
        User trueUser = userService.findByEmail(user.getEmail());

        if (trueUser == null) {
            redir.addFlashAttribute("normalErrorMessage", "There is no such account! Please register first.");
            return "redirect:/login";
        }

        // You should not encrypt the raw password yourself.
        boolean match = bCryptPasswordEncoder.matches(user.getPassword(), trueUser.getPassword());
        if (match) {
            if (trueUser.getEnabled()) {
                // Set session to authenticate access
                session.setAttribute("user", trueUser);
                // TODO Add different roles for different user type - "ORG", "MANAGER"
                session.setAttribute("role", "USER");
                return "redirect:/user";
            } else {
                model.addAttribute("normalErrorMessage", "Please activate your account first!");
                return "login";
            }
        } else {
            model.addAttribute("normalErrorMessage", "Wrong password!");
            return "login";
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
