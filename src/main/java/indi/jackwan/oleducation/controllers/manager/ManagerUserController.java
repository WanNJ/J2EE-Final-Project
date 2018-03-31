package indi.jackwan.oleducation.controllers.manager;

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
public class ManagerUserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/manager/users", method = RequestMethod.GET)
    public String showUsersPage(Model model, HttpSession session) {
        model.addAttribute("manager", session.getAttribute("manager"));

        List<User> userList = userService.findAllUsers();
        model.addAttribute("userList", userList);

        return "manager/users";
    }
}
