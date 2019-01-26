package com.example.demo;

import com.example.demo.entity.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/public/**")
                .addResourceLocations("classpath:/static/");
    }

//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/auth/login").setViewName("auth/login");
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(csrfTokenAddingInterceptor());
        registry.addInterceptor(authorityAddingInterceptor());
    }

    @Bean
    public HandlerInterceptor csrfTokenAddingInterceptor() {
        return new HandlerInterceptorAdapter() {
            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response,
                                   Object handler, ModelAndView view) {

                CsrfToken token = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
                if (token != null && view != null) {
                    view.addObject(token.getParameterName(), token);
                }
            }
        };
    }

    @Bean
    public HandlerInterceptor authorityAddingInterceptor() {
        return new HandlerInterceptorAdapter() {
            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response,
                                   Object handler, ModelAndView view) {

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                User user = null;
                if (authentication != null && view != null) {

                    if (authentication.getPrincipal() instanceof User)
                        user = (User) authentication.getPrincipal();

                    if (user == null)
                        user = new User();

                    view.addObject("principal", user);
                }
            }
        };
    }

}
