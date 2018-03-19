package indi.jackwan.oleducation.controllers;

import indi.jackwan.oleducation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String showRegistrationPage(Model model, HttpSession session) {
        model.addAttribute("user", session.getAttribute("user"));
        return "user";
    }
}
