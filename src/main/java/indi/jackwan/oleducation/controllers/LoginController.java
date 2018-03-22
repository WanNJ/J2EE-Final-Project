package indi.jackwan.oleducation.controllers;

import indi.jackwan.oleducation.models.Organization;
import indi.jackwan.oleducation.models.User;
import indi.jackwan.oleducation.service.OrgService;
import indi.jackwan.oleducation.service.UserService;
import indi.jackwan.oleducation.utils.Enums.LoginResult;
import org.springframework.beans.factory.annotation.Autowired;
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
    private UserService userService;
    @Autowired
    private OrgService orgService;

    @RequestMapping(value = "login/user", method = RequestMethod.GET)
    public String showUserLoginPage(Model model, User user, @RequestParam(value="message", required=false) String message) {
        if (message != null) {
            model.addAttribute("normalErrorMessage", message);
        }
        model.addAttribute("user", user);
        return "user/login";
    }

    @RequestMapping(value = "login/org", method = RequestMethod.GET)
    public String showOrgLoginPage(Model model, Organization organization) {
        model.addAttribute("org", organization);
        return "org/login";
    }

    @RequestMapping(value = "login/user", method = RequestMethod.POST)
    public String processUserLoginForm(Model model, User user, HttpSession session, RedirectAttributes redir) throws Exception {
        LoginResult loginResult = userService.login(user.getEmail(), user.getPassword());

        if (loginResult == LoginResult.NO_SUCH_ACCOUNT) {
            redir.addFlashAttribute("normalErrorMessage", "There is no such account! Please register first.");
            return "redirect:/login/user";
        } else if (loginResult == LoginResult.WRONG_PASSWORD) {
            model.addAttribute("normalErrorMessage", "Wrong password!");
            return "user/login";
        } else if (loginResult == LoginResult.NOT_ACTIVATED) {
            model.addAttribute("normalErrorMessage", "Please activate your account first!");
            return "user/login";
        } else if (loginResult == LoginResult.SUCCESS) {
            User currentUser = userService.findByEmail(user.getEmail());
            session.setAttribute("user", currentUser);
            session.setAttribute("role", "USER");
            return "redirect:/user";
        } else {
            throw new Exception("FATAL ERROR! LOGIN LOGIC INCOMPLETE!");
        }
    }

    @RequestMapping(value = "login/org", method = RequestMethod.POST)
    public String processOrgLoginForm(Model model, Organization organization, HttpSession session, RedirectAttributes redir) throws Exception {
        LoginResult loginResult = orgService.login(organization.getOrgCode(), organization.getPassword());

        if (loginResult == LoginResult.NO_SUCH_ACCOUNT) {
            redir.addFlashAttribute("errorMessage", "There is no such organization! Please register one first.");
            return "redirect:org/login";
        } else if (loginResult == LoginResult.WRONG_PASSWORD) {
            model.addAttribute("errorMessage", "Wrong password!");
            return "org/login";
        } else if (loginResult == LoginResult.NOT_ACTIVATED) {
            model.addAttribute("warningMessage", "Your application are still in line. Please wait.");
            return "org/login";
        } else if (loginResult == LoginResult.SUCCESS) {
            Organization currentOrg = orgService.findByOrgCode(organization.getOrgCode());
            session.setAttribute("org", organization);
            session.setAttribute("role", "ORG");
            return "redirect:/org";
        } else {
            throw new Exception("FATAL ERROR! LOGIN LOGIC INCOMPLETE!");
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redir) {
        userService.logout(session);
        redir.addFlashAttribute("normalInfoMessage", "You've just logged out successfully!");
        return "redirect:/login";
    }
}
