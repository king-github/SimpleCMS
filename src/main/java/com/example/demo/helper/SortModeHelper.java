package com.example.demo.helper;

import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class SortModeHelper {

    public enum OrderMode {

//        a(href="?page="+ pageable.number +"&size="+pageable['size']) none
//        a(href="?page="+ pageable.number +"&size="+pageable['size'] + "&sort=author.lastname&sort=author.firstname") by author - ascending
//        a(href="?page="+ pageable.number +"&size="+pageable['size'] + "&sort=author.lastname,desc&sort=author.firstname,desc") by author - descending
//        a(href="?page="+ pageable.number +"&size="+pageable['size'] + "&sort=title") by title - ascending
//        a(href="?page="+ pageable.number +"&size="+pageable['size'] + "&sort=title,desc") by title - descending
//        a(href="?page="+ pageable.number +"&size="+pageable['size'] + "&sort=date") by date - ascending
//        a(href="?page="+ pageable.number +"&size="+pageable['size'] + "&sort=date,desc") by date - descending

        NONE("none", ""),
        DATE("by date - ascending", "&sort=date,ASC"),
        DATE_DESC("by date -descending", "&sort=date,DESC"),
        AUTHOR("by author - ascending", "&sort=author.lastname,ASC&sort=author.firstname,ASC"),
        AUTHOR_DESC("by author - descending", "&sort=author.lastname,DESC&sort=author.firstname,DESC"),
        TITLE("by title - ascending", "&sort=title,ASC"),
        TITLE_DESC("by title - descending", "&sort=title,DESC")
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
