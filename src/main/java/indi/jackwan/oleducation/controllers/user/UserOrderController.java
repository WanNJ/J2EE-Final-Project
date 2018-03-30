package indi.jackwan.oleducation.controllers.user;

import indi.jackwan.oleducation.models.BankAccount;
import indi.jackwan.oleducation.models.User;
import indi.jackwan.oleducation.models.UserOrder;
import indi.jackwan.oleducation.service.OrderService;
import indi.jackwan.oleducation.service.PaymentService;
import indi.jackwan.oleducation.service.UserService;
import indi.jackwan.oleducation.utils.Enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserOrderController {
    @Autowired
    private UserService userService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PaymentService paymentService;

    @RequestMapping(value = "/user/orders", method = RequestMethod.GET)
    public String getOrderPage(Model model, HttpSession session, RedirectAttributes redir) {
        User currentUser = (User) session.getAttribute("user");
        model.addAttribute("user", currentUser);

        List<UserOrder> orderList = orderService.findUserOrdersByUser(currentUser);
        model.addAttribute("orderList", orderList);

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

        if (orderService.makeClassReservation(user, classId, userOrder)) {
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
        OrderStatus result = orderService.makeCourseReservation(user, courseId, userOrder);

        if (result == OrderStatus.WAITING_TO_BE_PAID) {
            // Actually, classes are assigned right after the order is placed.
            redir.addFlashAttribute("successMessage", "Your reservation has been made, please pay the bill within 15 minutes!" +
                    "Note that you will not be assigned to any class utill 2 weeks before the class begin. If we can't fulfill your requirements, full refund will be provided.");
        }
        else if (result == OrderStatus.INVALID) {
            redir.addFlashAttribute("errorMessage", "Oops, something went wrong with your order!");
        }
        else if (result == OrderStatus.UNSUCCESSFULL) {
            redir.addFlashAttribute("errorMessage", "You requirements cannot be fulfilled. You will not be charged for this order.");
        }
        return "redirect:/user/orders";
    }

    /*
     * TODO Authentication required. User can only get access to the orders that belong to him.
     */
    @RequestMapping(value = "/user/order/{orderId}", method = RequestMethod.GET)
    public String getOrder(Model model, HttpSession session, @PathVariable(value = "orderId") final int orderId,
                           @ModelAttribute(value = "bankAccount") BankAccount account) {
        User currentUser = (User) session.getAttribute("user");
        model.addAttribute("user", currentUser);

        UserOrder userOrder = orderService.findById(orderId);
        model.addAttribute("userOrder", userOrder);

        return "user/order";
    }

    @RequestMapping(value = "/user/order/{orderId}/placeorder", method = RequestMethod.POST)
    public String placeOrder(Model model, HttpSession session, @PathVariable(value = "orderId") final int orderId,
                             @ModelAttribute(value = "bankAccount") BankAccount account, RedirectAttributes redir) {
        User currentUser = (User) session.getAttribute("user");
        UserOrder userOrder = orderService.findById(orderId);

        if (paymentService.validate(account)) {
            if(orderService.payForOrder(currentUser, userOrder, account)) {
                redir.addFlashAttribute("successMessage", "Your order has been paid!");
            } else {
                redir.addFlashAttribute("errorMessage", "Oops, something went wrong! Please try again later.");
            }
        } else {
            redir.addFlashAttribute("errorMessage", "Wrong password or account address!");
        }

        return "redirect:/user/order/" + orderId;
    }
}
