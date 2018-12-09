package com.example.demo.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import java.util.Locale;
import java.util.Map;

@Component
public class FormHelperFactory {

    @Autowired
    private MessageSource messageSource;

    public FormHelper makeErrorFormHelper(BindingResult bindingResult) {

        Locale locale = LocaleContextHolder.getLocale();
        return new FormHelperBindingResult(messageSource, locale, bindingResult);
    }

    public FormHelper makeErrorFormHelper(Map<String, Object> map) {

        Locale locale = LocaleContextHolder.getLocale();
        return new FormHelperMap(messageSource, locale, map);
    }
}
