package indi.jackwan.oleducation.controllers;

import indi.jackwan.oleducation.models.User;
import indi.jackwan.oleducation.service.UserService;
import indi.jackwan.oleducation.utils.Enums.LoginResult;
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
    public String processLoginForm(Model model, User user, HttpSession session, RedirectAttributes redir) throws Exception {
        LoginResult loginResult = userService.login(user.getEmail(), user.getPassword());

        if (loginResult == LoginResult.NO_SUCH_ACCOUNT) {
            redir.addFlashAttribute("normalErrorMessage", "There is no such account! Please register first.");
            return "redirect:/login";
        }
        else if (loginResult == LoginResult.WRONG_PASSWORD) {
            model.addAttribute("normalErrorMessage", "Wrong password!");
            return "login";
        } else if (loginResult == LoginResult.NOT_ACTIVATED) {
            model.addAttribute("normalErrorMessage", "Please activate your account first!");
            return "login";
        } else if (loginResult == LoginResult.SUCCESS) {
            User currentUser = userService.findByEmail(user.getEmail());
            session.setAttribute("user", currentUser);
            // TODO Set Different roles to different users.
            session.setAttribute("role", "USER");
            return "redirect:/user";
        } else {
            throw new Exception("FATAL ERROR! LOGIC INCOMPLETE!");
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redir) {
        session.invalidate();
        redir.addFlashAttribute("normalInfoMessage", "You've just logged out successfully!");
        return "redirect:/login";
    }
}
