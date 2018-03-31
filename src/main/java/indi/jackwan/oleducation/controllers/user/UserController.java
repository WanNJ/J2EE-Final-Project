package indi.jackwan.oleducation.controllers.user;

import indi.jackwan.oleducation.models.Class;
import indi.jackwan.oleducation.models.User;
import indi.jackwan.oleducation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String showRegistrationPage(Model model, HttpSession session) {
        User old = (User) session.getAttribute("user");
        User user = userService.findByEmail(old.getEmail());
        model.addAttribute("user", user);

        List<Class> classList = user.getClassList();
        model.addAttribute("classList", classList);

        return "user/dashboard";
    }
}
