package indi.jackwan.oleducation.controllers.user;

import indi.jackwan.oleducation.models.User;
import indi.jackwan.oleducation.models.UserOrder;
import indi.jackwan.oleducation.service.OrderService;
import indi.jackwan.oleducation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class UserOrderController {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/user/orders", method = RequestMethod.GET)
    public String getOrderPage(Model model, HttpSession session, RedirectAttributes redir) {
        model.addAttribute("user", session.getAttribute("user"));
        return "user/orders";
    }

    /**
     * Make reservations with classes specified.
     */
    @RequestMapping(value = "/user/order/course/{courseId}/class/{classId}/reserve", method = RequestMethod.POST)
    public String makeClassReservation(Model model, HttpSession session, RedirectAttributes redir,
                                  @PathVariable(value = "courseId") final int courseId, @PathVariable(value = "classId") final int classId,
                                  @ModelAttribute(value = "userOrder") UserOrder userOrder) {
        User user = (User) session.getAttribute("user");

        if(orderService.makeClassReservation(user, classId, userOrder)) {
            redir.addFlashAttribute("successMessage", "Your reservation has been made, please pay the bill within 15 minutes!");
        } else {
            redir.addFlashAttribute("errorMessage", "Oops, something went wrong!");
        }
        return "redirect:/user/orders";
    }

    @RequestMapping(value = "/user/order/course/{courseId}/reserve", method = RequestMethod.POST)
    public String makeCourseReservation(Model model, HttpSession session, RedirectAttributes redir,
                                        @PathVariable(value = "courseId") final int courseId,
                                        @ModelAttribute(value = "userOrder") UserOrder userOrder) {
        User user = (User) session.getAttribute("user");
        if(orderService.makeCourseReservation(user, courseId, userOrder)) {
            redir.addFlashAttribute("successMessage", "Your reservation has been made, please pay the bill within 15 minutes!");
        } else {
            redir.addFlashAttribute("errorMessage", "Oops, something went wrong!");
        }
        return "redirect:/user/orders";
    }
}
