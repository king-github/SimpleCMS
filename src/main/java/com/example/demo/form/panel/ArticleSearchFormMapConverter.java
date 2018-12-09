package com.example.demo.form.panel;


import java.util.HashMap;
import java.util.Map;

public class ArticleSearchFormMapConverter {

    public static Map<String, Object> from (ArticleSearchForm articleSearchForm) {

        HashMap<String, Object> map = new HashMap<>();

        map.put("title", articleSearchForm.getTitle());
        map.put("firstname", articleSearchForm.getFirstname());
        map.put("lastname", articleSearchForm.getLastname());
        map.put("sectionId", articleSearchForm.getSectionId());
        map.put("dateFrom", articleSearchForm.getDateFrom());
        map.put("dateTo", articleSearchForm.getDateTo());
        map.put("published", articleSearchForm.getPublished());

        return map;
    }

}
