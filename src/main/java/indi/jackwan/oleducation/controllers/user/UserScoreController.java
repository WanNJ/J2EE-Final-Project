package indi.jackwan.oleducation.controllers.user;

import indi.jackwan.oleducation.models.User;
import indi.jackwan.oleducation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class UserScoreController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user/score", method = RequestMethod.GET)
    public String getScorePage(Model model, HttpSession session, RedirectAttributes redir) {
        User old = (User) session.getAttribute("user");
        session.setAttribute("user", userService.findByEmail(old.getEmail()));
        model.addAttribute("user", session.getAttribute("user"));
        return "user/score";
    }

    @RequestMapping(value = "/user/score", method = RequestMethod.POST)
    public String transferScore(Model model, HttpSession session, RedirectAttributes redir,
                                @RequestParam("transferScoreAmount") double amount, @RequestParam("bankAccount") String bankAccount) {
        return "redirect:/user/score";
    }
}
