package com.example.demo.helper;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Sort;

import static org.junit.Assert.assertEquals;

public class PagerParamsHelperTest {

    private PagerParamsHelper pagerParamsHelper;

    @Before
    public void setUp() {

        pagerParamsHelper = PagerParamsHelper.of(5, 20, Sort.by("name", "cost"));
    }

    @Test
    public void build() {

        String result = pagerParamsHelper.build();

        String expect = "page=5&size=20&sort=name,ASC&sort=cost,ASC";

        assertEquals(expect, result);
    }

    @Test
    public void buildWithPage() {

        String result1 = pagerParamsHelper.withPage(12).build();
        String result2 = pagerParamsHelper.build();
        String result3 = pagerParamsHelper.build();

        String expect1 = "page=12&size=20&sort=name,ASC&sort=cost,ASC";
        String expect2 = "page=5&size=20&sort=name,ASC&sort=cost,ASC";

        assertEquals(expect1, result1);
        assertEquals(expect2, result2);
        assertEquals(expect2, result3);
    }

    @Test
    public void buildWithSize() {

        String result1 = pagerParamsHelper.withSize(50).build();
        String result2 = pagerParamsHelper.build();
        String result3 = pagerParamsHelper.build();

        String expect1 = "page=5&size=50&sort=name,ASC&sort=cost,ASC";
        String expect2 = "page=5&size=20&sort=name,ASC&sort=cost,ASC";

        assertEquals(expect1, result1);
        assertEquals(expect2, result2);
        assertEquals(expect2, result3);
    }

    @Test
    public void buildWithSort() {

        String result1 = pagerParamsHelper.withSort(Sort.by("name").descending()).build();
        String result2 = pagerParamsHelper.build();
        String result3 = pagerParamsHelper.build();

        String expect1 = "page=5&size=20&sort=name,DESC";
        String expect2 = "page=5&size=20&sort=name,ASC&sort=cost,ASC";

        assertEquals(expect1, result1);
        assertEquals(expect2, result2);
        assertEquals(expect2, result3);
    }

    @Test
    public void buildWithPageSizeAndSort() {

        String result1 = pagerParamsHelper.withPage(33).withSize(45).withSort(Sort.by("name").descending()).build();
        String result2 = pagerParamsHelper.build();
        String result3 = pagerParamsHelper.build();

        String expect1 = "page=33&size=45&sort=name,DESC";
        String expect2 = "page=5&size=20&sort=name,ASC&sort=cost,ASC";

        assertEquals(expect1, result1);
        assertEquals(expect2, result2);
        assertEquals(expect2, result3);
    }

    @Test
    public void buildWithOutSort() {

        String result1 = pagerParamsHelper.withOutSort().build();
        String result2 = pagerParamsHelper.build();
        String result3 = pagerParamsHelper.build();

        String expect1 = "page=5&size=20";
        String expect2 = "page=5&size=20&sort=name,ASC&sort=cost,ASC";

        assertEquals(expect1, result1);
        assertEquals(expect2, result2);
        assertEquals(expect2, result3);
    }

    @Test
    public void buildWithStringSort() {

        String result1 = pagerParamsHelper.withSort("&sort=author.user.name,ASC&sort=author.user.age,DESC").build();
        String result2 = pagerParamsHelper.build();
        String result3 = pagerParamsHelper.build();

        String expect1 = "page=5&size=20&sort=author.user.name,ASC&sort=author.user.age,DESC";
        String expect2 = "page=5&size=20&sort=name,ASC&sort=cost,ASC";

        assertEquals(expect1, result1);
        assertEquals(expect2, result2);
        assertEquals(expect2, result3);
    }

    @Test
    public void buildEmpty() {

        pagerParamsHelper = PagerParamsHelper.of(null, null, null);

        String result1 = pagerParamsHelper.build();

        String expect1 = "";

        assertEquals(expect1, result1);
    }

    @Test
    public void buildEmptyWith() {

        pagerParamsHelper = PagerParamsHelper.of(null, null, null);

        String result1 = pagerParamsHelper.withPage(22).build();
        String result2 = pagerParamsHelper.withSize(60).build();
        String result3 = pagerParamsHelper.withSort(Sort.by("name", "cost")).build();
        String result4 = pagerParamsHelper.withPage(16).withSize(33).withSort(Sort.by("cost")).build();

        String expect1 = "page=22";
        String expect2 = "size=60";
        String expect3 = "sort=name,ASC&sort=cost,ASC";
        String expect4 = "page=16&size=33&sort=cost,ASC";

        assertEquals(expect1, result1);
        assertEquals(expect2, result2);
        assertEquals(expect3, result3);
        assertEquals(expect4, result4);
    }

}