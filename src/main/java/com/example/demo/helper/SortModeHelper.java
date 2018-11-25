package com.example.demo.helper;

import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class SortModeHelper {

    public enum OrderMode {

        NONE("none", ""),
        DATE("by date - ascending", "&sort=publicationDate,ASC"),
        DATE_DESC("by date - descending", "&sort=publicationDate,DESC"),
        AUTHOR("by author - ascending", "&sort=author.lastname,ASC&sort=author.firstname,ASC"),
        AUTHOR_DESC("by author - descending", "&sort=author.lastname,DESC&sort=author.firstname,DESC"),
        TITLE("by title - ascending", "&sort=title,ASC"),
        TITLE_DESC("by title - descending", "&sort=title,DESC"),
        SECTION("by section - ascending", "&sort=section.name,ASC"),
        SECTION_DESC("by section - descending", "&sort=section.name,DESC")
        ;

        private String name;
        private String paramString;

        OrderMode(String name, String paramString) {
            this.name = name;
            this.paramString = paramString;
        }

        public String getName() {
            return name;
        }

        public String getParamString() {
            return paramString;
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
