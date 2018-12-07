package com.example.demo.helper;

import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ErrorFormHelper {

    private MessageSource messageSource;

    private Locale locale;

    private BindingResult bindingResult;

    public ErrorFormHelper(MessageSource messageSource, Locale locale, BindingResult bindingResult) {
        this.messageSource = messageSource;
        this.locale = locale;
        this.bindingResult = bindingResult;
    }

    public List<String> getMessages(String fieldName) {

        List<String> msgs = new ArrayList<>();

        if (bindingResult != null && bindingResult.hasFieldErrors(fieldName)) {
            for (FieldError fieldError : bindingResult.getFieldErrors(fieldName)) {
                msgs.add(messageSource.getMessage(fieldError, locale));
            }
        }

        return msgs;
    }

    public boolean isValid(String fieldName) {

        if (bindingResult != null && bindingResult.hasFieldErrors(fieldName))
            return false;

        return true;
    }

    public boolean isNotValid(String fieldName) {

        return !isValid(fieldName);
    }

    public String getValidCss(String fieldName, String validCss, String invalidCss) {

        return (isValid(fieldName)) ? validCss : invalidCss;
    }

}
