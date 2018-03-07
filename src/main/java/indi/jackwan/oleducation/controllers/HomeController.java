package indi.jackwan.oleducation.controllers;

import indi.jackwan.oleducation.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    // This means to get the bean called UserRepository, which is auto-generated by Spring, we will use it to handle the data.
    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value="/register")
    public String greeting(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "home";
    }

}
