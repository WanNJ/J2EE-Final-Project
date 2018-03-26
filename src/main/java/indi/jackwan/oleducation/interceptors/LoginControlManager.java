package indi.jackwan.oleducation.interceptors;

import indi.jackwan.oleducation.models.Manager;
import indi.jackwan.oleducation.models.Organization;
import indi.jackwan.oleducation.models.User;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginControlManager extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws IOException {

        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("user");
        Organization org = (Organization) session.getAttribute("org");
        Manager manager = (Manager) session.getAttribute("manager");
        boolean loggedIn = !((user == null) && (org == null) && (manager == null));
        if (user != null) {
            response.sendRedirect("/user");
        } else if (org != null) {
            response.sendRedirect("/org");
        } else if (manager != null) {
            response.sendRedirect("/manager");
        } else {
            return true;
        }
        return false;
    }
}