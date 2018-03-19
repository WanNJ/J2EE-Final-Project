package indi.jackwan.oleducation.configurations;

import indi.jackwan.oleducation.interceptors.AccessManager;
import indi.jackwan.oleducation.interceptors.LoginControlManager;
import indi.jackwan.oleducation.interceptors.SessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
    LoginControlManager getLoginControlManager() { return new LoginControlManager(); }

    @Bean
    SessionManager getSessionManager() { return new SessionManager(); }

    @Bean
    AccessManager getUserAccessManager() {return new AccessManager("USER"); }

    @Bean
    AccessManager getOrgAccessManager() {return new AccessManager("ORG"); }

    @Bean
    AccessManager getManagerAccessManager() {return new AccessManager("MANAGER"); }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
	    registry.addInterceptor(getLoginControlManager()).addPathPatterns("/login", "/register");
	    registry.addInterceptor(getSessionManager())
                .addPathPatterns("/user/**", "/org/**", "/manager/**");
        // assuming you put your serve your static files with /resources/ mapping
        // and the login and register page is served with "/login" and "/register" mapping

        registry.addInterceptor(getUserAccessManager()).addPathPatterns("/user/**");
        registry.addInterceptor(getOrgAccessManager()).addPathPatterns("/org/**");
        registry.addInterceptor(getManagerAccessManager()).addPathPatterns("/manager/**");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("redirect:/login");
    }
}