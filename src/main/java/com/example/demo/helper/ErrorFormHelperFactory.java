package com.example.demo.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.Locale;

@Component
public class ErrorFormHelperFactory {

    @Autowired
    private MessageSource messageSource;

    public ErrorFormHelper makeErrorFormHelper(BindingResult bindingResult) {

        Locale locale = LocaleContextHolder.getLocale();
        return new ErrorFormHelper(messageSource, locale, bindingResult);
    }
}
