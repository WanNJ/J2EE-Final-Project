package indi.jackwan.oleducation.interceptors;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AccessManager extends HandlerInterceptorAdapter {
    private String role;

    public AccessManager(String role) {
        this.role = role;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) {

        HttpSession session = request.getSession(false);
        String role = (String) session.getAttribute("role");

        if(role.equals(this.role)) {
            return true;
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }
    }
}
