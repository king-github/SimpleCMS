package com.example.demo.helper;

import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class FormHelperMap implements FormHelper {

    private MessageSource messageSource;

    private Locale locale;

    private Map<String, Object> map;

    public FormHelperMap(MessageSource messageSource, Locale locale, Map<String, Object> map) {
        this.messageSource = messageSource;
        this.locale = locale;
        this.map = map;
    }

    @Override
    public List<String> getErrorsFor(String fieldName) {

        return new ArrayList<>();
    }

    @Override
    public Object getValueFor(String fieldName) {

        return map.get(fieldName);
    }

    @Override
    public boolean isValid(String fieldName) {

        return true;
    }

    @Override
    public boolean isNotValid(String fieldName) {

        return false;
    }

    @Override
    public String getValidCss(String fieldName, String validCss, String invalidCss) {

        return (isValid(fieldName)) ? validCss : invalidCss;
    }

}
