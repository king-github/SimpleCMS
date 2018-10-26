package com.example.demo.controller;

import com.example.demo.error.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@ControllerAdvice
public class ExceptionHandlerController {

    public static final String DEFAULT_ERROR_VIEW = "default/error";
    public static final String NOT_FOUND_ERROR_VIEW = "default/error-404";

    @ExceptionHandler(value = {Exception.class, RuntimeException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView defaultErrorHandler(HttpServletRequest request, Exception e) {

        ModelAndView modelAndView = new ModelAndView(DEFAULT_ERROR_VIEW);

        modelAndView.addObject("datetime", new Date());
        modelAndView.addObject("exception", e);
        modelAndView.addObject("url", request.getRequestURL());
        return modelAndView;
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {NotFoundException.class})
    public ModelAndView notFoundErrorHandler(HttpServletRequest request, Exception e) {

        ModelAndView modelAndView = new ModelAndView(NOT_FOUND_ERROR_VIEW);

        modelAndView.addObject("datetime", new Date());
        modelAndView.addObject("exception", e);
        modelAndView.addObject("url", request.getRequestURL());
        return modelAndView;
    }
}