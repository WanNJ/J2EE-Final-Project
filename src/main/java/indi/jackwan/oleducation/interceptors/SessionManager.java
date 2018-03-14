package indi.jackwan.oleducation.interceptors;

import indi.jackwan.oleducation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SessionManager implements HandlerInterceptor {
    @Autowired
    UserService userService;

    // This method is called before the controller
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws IOException {

        HttpSession session = request.getSession(true);
        boolean loggedIn = !(session.getAttribute("user") == null);
        if(loggedIn) {
            return true;
        }
        else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            // "return false" will send a 401 with no response body.
            response.sendRedirect("/login");
        }
        return false;
    }

    // After a request is handled by a request handler.
    // It gives access to the returned ModelAndView object,
    // so you can manipulate the model attributes in it.
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
    }

    // After the completion of all request processing i.e. after the view has been rendered.
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex) {

    }
}