package com.example.demo.helper;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class SortModeHelper {

    private static int SHORT_LIST = 1;
    private static int LONG_LIST = 2;
    private static int FULL_LIST = SHORT_LIST + LONG_LIST;

    public enum OrderMode {

        NONE("none", "", false, FULL_LIST),
        ID("by id - ascending", "&sort=id,ASC", false, LONG_LIST),
        ID_DESC("by id - descending", "&sort=id,DESC", true, LONG_LIST),
        DATE("by date - ascending", "&sort=publicationDate,ASC", false, FULL_LIST),
        DATE_DESC("by date - descending", "&sort=publicationDate,DESC", true, FULL_LIST),
        AUTHOR("by author - ascending", "&sort=author.lastname,ASC&sort=author.firstname,ASC", false, FULL_LIST),
        AUTHOR_DESC("by author - descending", "&sort=author.lastname,DESC&sort=author.firstname,DESC", true, FULL_LIST),
        TITLE("by title - ascending", "&sort=title,ASC", false, FULL_LIST),
        TITLE_DESC("by title - descending", "&sort=title,DESC", true, FULL_LIST),
        SECTION("by section - ascending", "&sort=section.name,ASC", false, FULL_LIST),
        SECTION_DESC("by section - descending", "&sort=section.name,DESC", true, FULL_LIST),
        PUBLISHED("by not published first", "&sort=published,ASC", false, LONG_LIST),
        PUBLISHED_DESC("by published first ", "&sort=published,DESC", true, LONG_LIST)
        ;

        private int listType;
        private String name;
        private String paramString;
        private boolean desc;

        OrderMode(String name, String paramString, boolean desc, int listType) {
            this.name = name;
            this.paramString = paramString;
            this.desc = desc;
            this.listType = listType;
        }

        public String getName() {
            return name;
        }

        public String getParamString() {
            return paramString;
        }

        public boolean isDesc() {
            return desc;
        }
    }


    public List<OrderMode> getAllModes() {

        return Stream.of(OrderMode.values())
               .filter(orderMode -> (orderMode.listType & LONG_LIST) != 0)
               .collect(Collectors.toList())
               ;
    }

    public List<OrderMode> getShortListOfModes() {

        return Stream.of(OrderMode.values())
                .filter(orderMode -> (orderMode.listType & SHORT_LIST) != 0)
                .collect(Collectors.toList())
                ;
    }

    public OrderMode findModeByParamString(String param) {

        return Stream.of(OrderMode.values())
                .filter(orderMode -> param.equals(orderMode.getParamString()))
                .findAny()
                .orElse(OrderMode.NONE)
                ;
    }
}
