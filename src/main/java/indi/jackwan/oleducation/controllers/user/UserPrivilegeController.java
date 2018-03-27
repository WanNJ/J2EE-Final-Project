package indi.jackwan.oleducation.controllers.user;

import indi.jackwan.oleducation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class UserPrivilegeController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user/previlege", method = RequestMethod.GET)
    public String getPrivilegePage(Model model, HttpSession session, RedirectAttributes redir) {
        model.addAttribute("user", session.getAttribute("user"));
        return "user/privilege";
    }
}
