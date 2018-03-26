package indi.jackwan.oleducation.controllers.user;

import com.nulabinc.zxcvbn.Strength;
import com.nulabinc.zxcvbn.Zxcvbn;
import indi.jackwan.oleducation.models.User;
import indi.jackwan.oleducation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

/**
 * TODO Refactor needed, service logic needs to be moved into service beans instead of staying in controllers.
 * TODO Remember to refactor if there's enough time.
 */

@RequestMapping(value = "/user/info")
@Controller
public class UserInfoController {
    @Autowired
    private UserService userService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping(value = "/changeNickname", method = RequestMethod.POST)
    public String changeNickname(User user, Model model, HttpSession session, RedirectAttributes redir) {
        if(user.getNickname().equals("")) {
            redir.addFlashAttribute("dangerMessage", "Nickname cannot be empty!");
        } else {
            User currentUser = (User) session.getAttribute("user");
            currentUser.setNickname(user.getNickname());
            userService.save(currentUser);
            redir.addFlashAttribute("successMessage", "You've just changed your nickname successfully!");
        }
        return "redirect:/user";
    }

    @RequestMapping(value = "changePassword", method = RequestMethod.POST)
    public String changePassword(User user, Model model, HttpSession session, RedirectAttributes redir) {
        User currentUser = (User) session.getAttribute("user");
        Zxcvbn passwordCheck = new Zxcvbn();

        Strength strength = passwordCheck.measure(user.getPassword());

        if (strength.getScore() < 3) {
            redir.addFlashAttribute("warningMessage", "Your password is too weak.  Choose a stronger one.");
            return "redirect:/user";
        }

        // Set bCrpyted Password to improve security
        currentUser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userService.save(currentUser);
        session.setAttribute("user", currentUser);

        redir.addFlashAttribute("successMessage", "Your password has been reset successfully!");
        return "redirect:/user";
    }

    @RequestMapping(value = "cancelMembership", method = RequestMethod.POST)
    public String cancelMembership(Model model, HttpSession session, RedirectAttributes redir) {
        User currentUser = (User) session.getAttribute("user");
        currentUser.setVip(false);
        userService.save(currentUser);
        redir.addFlashAttribute("successMessage", "You just cancelled your membership!");
        return "redirect:/user";
    }
}
