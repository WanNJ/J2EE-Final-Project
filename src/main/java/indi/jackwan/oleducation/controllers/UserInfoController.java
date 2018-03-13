package indi.jackwan.oleducation.controllers;

import indi.jackwan.oleducation.models.User;
import indi.jackwan.oleducation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserInfoController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String showRegistrationPage(Model model, User user) {
        model.addAttribute("user", user);
        return "user";
    }
}
