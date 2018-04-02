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
public class UserPrivilegeController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user/previlege", method = RequestMethod.GET)
    public String getPrivilegePage(Model model, HttpSession session, RedirectAttributes redir) {
        User old = (User) session.getAttribute("user");
        User current = userService.findById(old.getId());
        model.addAttribute("user", current);

        int vipClass;

        if (current.getExpenditure() < 100) {
            vipClass = 1;
        } else if (current.getExpenditure() < 200) {
            vipClass = 2;
        } else if (current.getExpenditure() < 300) {
            vipClass = 3;
        } else {
            vipClass = 4;
        }

        model.addAttribute("vipClass", vipClass);

        return "user/privilege";
    }
}
