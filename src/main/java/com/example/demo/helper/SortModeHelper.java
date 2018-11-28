package com.example.demo.helper;

import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class SortModeHelper {

    public enum OrderMode {

        NONE("none", "", false),
        DATE("by date - ascending", "&sort=publicationDate,ASC", false),
        DATE_DESC("by date - descending", "&sort=publicationDate,DESC", true),
        AUTHOR("by author - ascending", "&sort=author.lastname,ASC&sort=author.firstname,ASC", false),
        AUTHOR_DESC("by author - descending", "&sort=author.lastname,DESC&sort=author.firstname,DESC", true),
        TITLE("by title - ascending", "&sort=title,ASC", false),
        TITLE_DESC("by title - descending", "&sort=title,DESC", true),
        SECTION("by section - ascending", "&sort=section.name,ASC", false),
        SECTION_DESC("by section - descending", "&sort=section.name,DESC", true)
        ;

        private String name;
        private String paramString;
        private boolean desc;

        OrderMode(String name, String paramString, boolean desc) {
            this.name = name;
            this.paramString = paramString;
            this.desc = desc;
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


    public OrderMode[] getAllModes() {

        return OrderMode.values();
    }

    public OrderMode findModeByParamString(String param) {

        return Stream.of(OrderMode.values())
                .filter(orderMode -> param.equals(orderMode.getParamString()))
                .findAny()
                .orElse(OrderMode.NONE);
    }
}
