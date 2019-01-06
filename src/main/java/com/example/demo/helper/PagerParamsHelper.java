package com.example.demo.helper;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


public class PagerParamsHelper {

    private Integer pageParam;
    private Integer sizeParam;
    private String sortParam;

    private Integer pageParamTemp;
    private Integer sizeParamTemp;
    private String sortParamTemp;


    private String cache;
    private boolean modified;

    private PagerParamsHelper() {}

    private PagerParamsHelper(Integer pageParam, Integer sizeParam, String sortParam) {
        this.pageParam = pageParam;
        this.sizeParam = sizeParam;
        this.sortParam = sortParam;
    }

    public static PagerParamsHelper of(Integer page, Integer size, Sort sort) {

        return new PagerParamsHelper(page, size, getSortParamsString(sort));
    }

    public static PagerParamsHelper of(Pageable pageable) {

        if (pageable == null) return new PagerParamsHelper();

        return new PagerParamsHelper(pageable.getPageNumber(),
                                     pageable.getPageSize(),
                                     getSortParamsString(pageable.getSort()));
    }

    public static PagerParamsHelper of(Sort sort) {

        return new PagerParamsHelper(null,
                                     null,
                                     getSortParamsString(sort));
    }

    public String build(){

        if (cache == null || modified) {
            cache = buildParamsString((pageParamTemp != null) ? pageParamTemp : pageParam,
                    (sizeParamTemp != null) ? sizeParamTemp : sizeParam,
                    (sortParamTemp != null) ? sortParamTemp : sortParam
            );
            if(modified && pageParamTemp==null && sizeParamTemp==null && sortParamTemp==null) {
                modified = false;
            }
            pageParamTemp = null;
            sizeParamTemp = null;
            sortParamTemp = null;
        }
        return cache;
    }

    public PagerParamsHelper withPage(long page) {

        pageParamTemp = (int) page;
        modified = true;
        return this;
    }

    public PagerParamsHelper withSize(long size) {

        sizeParamTemp = (int) size;
        modified = true;
        return this;
    }

    public PagerParamsHelper withSort(Sort sort) {

        sortParamTemp = getSortParamsString(sort);
        modified = true;
        return this;
    }

    public PagerParamsHelper withSort(String sort) {

        sortParamTemp = sort;
        modified = true;
        return this;
    }

    public PagerParamsHelper withOutSort() {

        sortParamTemp = "";
        modified = true;
        return this;
    }


    private String buildParamsString(Integer page, Integer size, String sort) {

        StringBuilder stringBuilder = new StringBuilder();
        if (page!=null) {
            stringBuilder
                    .append("&page=")
                    .append(page);
        }
        if (size!=null) {
            stringBuilder
                    .append("&size=")
                    .append(size);
        }
        stringBuilder.append(sort);

        if (stringBuilder.length() >0 ) {
            stringBuilder.deleteCharAt(0);
        }
        return stringBuilder.toString();
    }

    private String buildParamsString(String sort) {

        if (sort.startsWith("&")) {
            sort = sort.substring(1);
        }

        return new StringBuilder()
                .append(sort)
                .toString();
    }

    public static String getSortParamsString(Pageable pageable) {

        return getSortParamsString(pageable.getSort());
    }

    public static String getSortParamsString(Sort sort) {

        StringBuilder paramString = new StringBuilder();
        if (sort != null)
            for (Sort.Order s : sort) {
                paramString.append("&sort=")
                           .append(s.getProperty())
                           .append(",")
                           .append(s.getDirection());
            }
        return paramString.toString();
    }


}
