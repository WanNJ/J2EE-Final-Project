package indi.jackwan.oleducation.controllers;

import indi.jackwan.oleducation.models.User;
import indi.jackwan.oleducation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String showRegistrationPage(Model model, User user) {
        model.addAttribute("user", user);
        return "login";
    }

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public String processLoginForm(Model model, User user) {
        //TODO To figure it out
        boolean match = bCryptPasswordEncoder.matches(user.getPassword(), userService.findByEmail(user.getEmail()).getPassword());
        if(match) {
            return "redirect:user";
        } else {
            model.addAttribute("normalErrorMessage", "Wrong password!");
            return "login";
        }
    }
}
