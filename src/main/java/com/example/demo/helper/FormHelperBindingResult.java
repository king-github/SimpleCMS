package com.example.demo.helper;

import org.springframework.context.MessageSource;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.*;


public class FormHelperBindingResult implements FormHelper {

    private MessageSource messageSource;

    private Locale locale;

    private BindingResult bindingResult;

    public FormHelperBindingResult(MessageSource messageSource, Locale locale, BindingResult bindingResult) {
        this.messageSource = messageSource;
        this.locale = locale;
        this.bindingResult = bindingResult;
    }

    @Override
    public List<String> getErrorsFor(String fieldName) {

        List<String> msgs = new ArrayList<>();

        if (bindingResult != null && bindingResult.hasFieldErrors(fieldName)) {
            for (FieldError fieldError : bindingResult.getFieldErrors(fieldName)) {
                msgs.add(messageSource.getMessage(fieldError, locale));
            }
        }

        return msgs;
    }

    @Override
    public List<String> getErrorsForForm() {

        List<String> msgs = new ArrayList<>();

        if (bindingResult != null && bindingResult.hasGlobalErrors()) {
            for (ObjectError objectError : bindingResult.getGlobalErrors()) {
                msgs.add(messageSource.getMessage(objectError, locale));
            }
        }

        return msgs;
    }

    @Override
    public Object getValueFor(String fieldName) {

        if (bindingResult != null)
            return  (bindingResult.getRawFieldValue(fieldName) instanceof Collection<?>)
                        ? bindingResult.getRawFieldValue(fieldName) : bindingResult.getFieldValue(fieldName);

        return null;
    }

    @Override
    public boolean isValid(String fieldName) {

        if (bindingResult != null && bindingResult.hasFieldErrors(fieldName))
            return false;

        return true;
    }

    @Override
    public boolean isNotValid(String fieldName) {

        return !isValid(fieldName);
    }

    @Override
    public String getValidCss(String fieldName, String validCss, String invalidCss) {

        return (isValid(fieldName)) ? validCss : invalidCss;
    }

    @Override
    public boolean isGlobalValid() {

        if (bindingResult != null && bindingResult.hasGlobalErrors())
            return false;

        return true;
    }

    @Override
    public boolean isGlobalNotValid() {

        return !isGlobalValid();
    }


    @Override
    public String getValidCss(String validCss, String invalidCss) {

        return (isGlobalValid()) ? validCss : invalidCss;
    }

}
