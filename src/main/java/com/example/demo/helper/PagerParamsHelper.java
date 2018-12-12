package com.example.demo.helper;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;


@Component
public class PagerParamsHelper {

    public String getParamsString(Pageable pageable) {

        return String.format("page=%d&size=%d%s",
                pageable.getPageNumber(), pageable.getPageSize(), getSortParamsString(pageable));
    }

    public String getSortParamsString(Pageable pageable) {

        String paramString = "";
        if (pageable.getSort() != null)
            for (Sort.Order sort : pageable.getSort()) {
                paramString += String.format("&sort=%s,%s", sort.getProperty(), sort.getDirection());
            }
        return paramString;
    }

    public String getSizeAndSortParamsString(Pageable pageable) {

        String paramString = String.format("&size=%d", pageable.getPageSize());
        if (pageable.getSort() != null)
            for (Sort.Order sort : pageable.getSort()) {
                paramString += String.format("&sort=%s,%s", sort.getProperty(), sort.getDirection());
            }
        return paramString;
    }

}
