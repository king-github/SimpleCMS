package com.example.demo.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class PageSizeHelper {

    @Value("#{'${page.size.list}'.split(',')}")
    private List<Integer> pageSizeList;

    public List<Integer> getPageSizeList () {

        return pageSizeList;
    }
}
