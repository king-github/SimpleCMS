package com.example.demo.helper;

import java.util.List;

public interface FormHelper {
    List<String> getErrorsFor(String fieldName);

    List<String> getErrorsForForm();

    Object getValueFor(String fieldName);

    boolean isValid(String fieldName);

    boolean isNotValid(String fieldName);

    String getValidCss(String fieldName, String validCss, String invalidCss);

    boolean isGlobalValid();

    boolean isGlobalNotValid();

    String getValidCss(String validCss, String invalidCss);
}
