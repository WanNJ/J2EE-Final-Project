package indi.jackwan.oleducation.interceptors;

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
        boolean loggedIn = !(user == null);
        if (loggedIn) {
            // TODO Send user to different url according to their roles.
            response.sendRedirect("/user");
        } else {
            return true;
        }
        return false;
    }
}