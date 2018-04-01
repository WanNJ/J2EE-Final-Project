package indi.jackwan.oleducation.controllers.manager;


import indi.jackwan.oleducation.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpSession;

@Controller
public class ManagerController {
    @Autowired
    private ManagerService managerService;

    @RequestMapping(value = "/manager", method = RequestMethod.GET)
    public String showManagerPage(Model model, HttpSession session) {
        model.addAttribute("manager", session.getAttribute("manager"));
        double[] financialStatisticData = managerService.getFinancialStatisticData();
        model.addAttribute("financialStatisticData", financialStatisticData);

        return "manager/dashboard";
    }
}
