package indi.jackwan.oleducation.interceptors;

import indi.jackwan.oleducation.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SessionManager extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws IOException {

        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("user");
        boolean loggedIn = !(user == null);
        if (loggedIn) {
            return true;
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            // "return false" will send a 401 with no response body.
            response.sendRedirect("/login?message=Please sign in first!");
        }
        return false;
    }
}