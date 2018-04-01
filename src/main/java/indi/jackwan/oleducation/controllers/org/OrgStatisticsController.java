package indi.jackwan.oleducation.controllers.org;

import indi.jackwan.oleducation.models.Organization;
import indi.jackwan.oleducation.models.UserOrder;
import indi.jackwan.oleducation.service.OrgService;
import indi.jackwan.oleducation.utils.statistics.OrgOrderStatisticSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class OrgStatisticsController {
    @Autowired
    private OrgService orgService;


    @RequestMapping(value = "/org/order-statistics", method = RequestMethod.GET)
    public String showOrderStatisticsPage(Model model, HttpSession session) {
        Organization organization = (Organization) session.getAttribute("org");
        List<UserOrder> userOrderList = orgService.getAllOrdersByOrg(organization);
        OrgOrderStatisticSet orderStatisticSet = orgService.getOrgOrderStatisticSet(organization);

        model.addAttribute("org", organization);
        model.addAttribute("userOrderList", userOrderList);
        model.addAttribute("orderStatisticSet", orderStatisticSet);

        return "org/order-statistics";
    }

    @RequestMapping(value = "/org/financial-statistics", method = RequestMethod.GET)
    public String showFinancialStatisticsPage(Model model, HttpSession session) {
        Organization organization = (Organization) session.getAttribute("org");

        double totalOrderAmount = orgService.getTotalOrderAmountByOrg(organization);
        double unpaidAmount = orgService.getUnpaidAmountByOrgId(organization.getId());

        model.addAttribute("org", organization);
        model.addAttribute("totalOrderAmount", totalOrderAmount);
        model.addAttribute("unpaidAmount", unpaidAmount);

        return "org/financial-statistics";
    }
}
