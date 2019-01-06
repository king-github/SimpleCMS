package com.example.demo.configuration;

import com.example.demo.helper.OrderModeHelper;
import com.example.demo.helper.order.OrderMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Configuration
public class OrderModesBean {


    @Bean(name = "articlePanelOrderModeHelper")
    public OrderModeHelper articlePanelOrderModeHelper() {

        ArrayList<OrderMode> list = new ArrayList<>();

        final OrderMode NONE = new OrderMode("NONE", "none", "", false);
        list.add(NONE);
        list.add(new OrderMode("ID", "by id - ascending", "&sort=id,ASC", false));
        list.add(new OrderMode("ID_DESC", "by id - descending", "&sort=id,DESC", true));
        list.add(new OrderMode("TITLE", "by title - ascending", "&sort=title,ASC", false));
        list.add(new OrderMode("TITLE_DESC", "by title - descending", "&sort=title,DESC", true));
        list.add(new OrderMode("AUTHOR", "by author - ascending", "&sort=author.lastname,ASC&sort=author.firstname,ASC", false));
        list.add(new OrderMode("AUTHOR_DESC", "by author - descending", "&sort=author.lastname,DESC&sort=author.firstname,DESC", true));
        list.add(new OrderMode("SECTION", "by section - ascending", "&sort=section.name,ASC", false));
        list.add(new OrderMode("SECTION_DESC", "by section - descending", "&sort=section.name,DESC", true));
        list.add(new OrderMode("DATE", "by date - ascending", "&sort=publicationDate,ASC", false));
        list.add(new OrderMode("DATE_DESC", "by date - descending", "&sort=publicationDate,DESC", true));
        list.add(new OrderMode("PUBLISHED", "by not published first", "&sort=published,ASC", false));
        list.add(new OrderMode("PUBLISHED_DESC", "by published first ", "&sort=published,DESC", true));

        return new OrderModeHelper(list, NONE);
    }

    @Bean(name="articleFrontOrderModeHelper")
    public OrderModeHelper articleFrontOrderModeHelper() {

        ArrayList<OrderMode> list = new ArrayList<>();

        final OrderMode NONE = new OrderMode("NONE", "none", "", false);
        list.add(NONE);

        list.add(new OrderMode("DATE", "by date - ascending", "&sort=publicationDate,ASC", false));
        list.add(new OrderMode("DATE_DESC", "by date - descending", "&sort=publicationDate,DESC", true));
        list.add(new OrderMode("AUTHOR", "by author - ascending", "&sort=author.lastname,ASC&sort=author.firstname,ASC", false));
        list.add(new OrderMode("AUTHOR_DESC", "by author - descending", "&sort=author.lastname,DESC&sort=author.firstname,DESC", true));
        list.add(new OrderMode("TITLE", "by title - ascending", "&sort=title,ASC", false));
        list.add(new OrderMode("TITLE_DESC", "by title - descending", "&sort=title,DESC", true));
        list.add(new OrderMode("SECTION", "by section - ascending", "&sort=section.name,ASC", false));
        list.add(new OrderMode("SECTION_DESC", "by section - descending", "&sort=section.name,DESC", true));

        return new OrderModeHelper(list, NONE);
    }

    @Bean(name="sectionPanelOrderModeHelper")
    public OrderModeHelper sectionPanelOrderModeHelper() {

        ArrayList<OrderMode> list = new ArrayList<>();

        final OrderMode NONE = new OrderMode("NONE", "none", "", false);
        list.add(NONE);

        list.add(new OrderMode("NAME", "by name - ascending", "&sort=name,ASC", false));
        list.add(new OrderMode("NAME_DESC", "by name - descending", "&sort=name,DESC", true));
        list.add(new OrderMode("QUANTITY", "by number of related articles - ascending", "&sort=quantity,ASC", false));
        list.add(new OrderMode("QUANTITY_DESC", "by number of related articles - descending", "&sort=quantity,DESC", true));
        list.add(new OrderMode("ID", "by id - ascending", "&sort=id,ASC", false));
        list.add(new OrderMode("ID_DESC", "by id - descending", "&sort=id,DESC", true));

        return new OrderModeHelper(list, NONE);
    }

}
