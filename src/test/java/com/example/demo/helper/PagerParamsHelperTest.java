package com.example.demo.helper;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Sort;

import static org.junit.Assert.assertEquals;

public class PagerParamsHelperTest {

    private PagerParamsHelper basePagerParamsHelper;

    @Before
    public void setUp() {

        basePagerParamsHelper = PagerParamsHelper.of(5, 20, Sort.by("name", "cost"));
    }

    @Test
    public void whenBuild_thenReceiveBase() {

        String result = basePagerParamsHelper.build();

        String expect = "page=5&size=20&sort=name,ASC&sort=cost,ASC";

        assertEquals(expect, result);
    }

    @Test
    public void givenPage_whenBuild_thenReceiveWithPage_nextReceiveBase() {

        String result1 = basePagerParamsHelper.withPage(12).build();
        String result2 = basePagerParamsHelper.build();
        String result3 = basePagerParamsHelper.build();

        String expect1 = "page=12&size=20&sort=name,ASC&sort=cost,ASC";
        String expect2 = "page=5&size=20&sort=name,ASC&sort=cost,ASC";

        assertEquals(expect1, result1);
        assertEquals(expect2, result2);
        assertEquals(expect2, result3);
    }

    @Test
    public void givenSize_whenBuild_thenReceiveWithSize_nextReceiveBase() {

        String result1 = basePagerParamsHelper.withSize(50).build();
        String result2 = basePagerParamsHelper.build();
        String result3 = basePagerParamsHelper.build();

        String expect1 = "page=5&size=50&sort=name,ASC&sort=cost,ASC";
        String expect2 = "page=5&size=20&sort=name,ASC&sort=cost,ASC";

        assertEquals(expect1, result1);
        assertEquals(expect2, result2);
        assertEquals(expect2, result3);
    }

    @Test
    public void givenSort_whenBuild_thenReceiveWithSort_nextReceiveBase() {

        String result1 = basePagerParamsHelper.withSort(Sort.by("name").descending()).build();
        String result2 = basePagerParamsHelper.build();
        String result3 = basePagerParamsHelper.build();

        String expect1 = "page=5&size=20&sort=name,DESC";
        String expect2 = "page=5&size=20&sort=name,ASC&sort=cost,ASC";

        assertEquals(expect1, result1);
        assertEquals(expect2, result2);
        assertEquals(expect2, result3);
    }

    @Test
    public void givenPageSizeSort_whenBuild_thenReceiveWithPageSizeSort_nextReceiveBase() {

        String result1 = basePagerParamsHelper.withPage(33).withSize(45).withSort(Sort.by("name").descending()).build();
        String result2 = basePagerParamsHelper.build();
        String result3 = basePagerParamsHelper.build();

        String expect1 = "page=33&size=45&sort=name,DESC";
        String expect2 = "page=5&size=20&sort=name,ASC&sort=cost,ASC";

        assertEquals(expect1, result1);
        assertEquals(expect2, result2);
        assertEquals(expect2, result3);
    }

    @Test
    public void givenBase_whenBuildWithOutSort_thenReceiveWithPageAndSortOnly_nextReceiveFullBase() {

        String result1 = basePagerParamsHelper.withOutSort().build();
        String result2 = basePagerParamsHelper.build();
        String result3 = basePagerParamsHelper.build();

        String expect1 = "page=5&size=20";
        String expect2 = "page=5&size=20&sort=name,ASC&sort=cost,ASC";

        assertEquals(expect1, result1);
        assertEquals(expect2, result2);
        assertEquals(expect2, result3);
    }

    @Test
    public void givenStringSort_whenBuild_thenReceiveWithStringSort_nextReceiveBase() {

        String result1 = basePagerParamsHelper.withSort("&sort=author.user.name,ASC&sort=author.user.age,DESC").build();
        String result2 = basePagerParamsHelper.build();
        String result3 = basePagerParamsHelper.build();

        String expect1 = "page=5&size=20&sort=author.user.name,ASC&sort=author.user.age,DESC";
        String expect2 = "page=5&size=20&sort=name,ASC&sort=cost,ASC";

        assertEquals(expect1, result1);
        assertEquals(expect2, result2);
        assertEquals(expect2, result3);
    }

    @Test
    public void givenEmpty_whenBuild_thenReceiveEmptyString() {

        basePagerParamsHelper = PagerParamsHelper.of(null, null, null);

        String result1 = basePagerParamsHelper.build();

        String expect1 = "";

        assertEquals(expect1, result1);
    }

    @Test
    public void givenEmptyAndPageSizeSort_whenBuild_thenReceiveWithPageSizeSort_nextReceiveEmpty() {

        PagerParamsHelper emptyPagerParamsHelper = PagerParamsHelper.of(null, null, null);

        String result1 = emptyPagerParamsHelper.withPage(22).build();
        String result2 = emptyPagerParamsHelper.withSize(60).build();
        String result3 = emptyPagerParamsHelper.withSort(Sort.by("name", "cost")).build();
        String result4 = emptyPagerParamsHelper.withPage(16).withSize(33).withSort(Sort.by("cost")).build();
        String result5 = emptyPagerParamsHelper.build();

        String expect1 = "page=22";
        String expect2 = "size=60";
        String expect3 = "sort=name,ASC&sort=cost,ASC";
        String expect4 = "page=16&size=33&sort=cost,ASC";
        String expect5 = "";

        assertEquals(expect1, result1);
        assertEquals(expect2, result2);
        assertEquals(expect3, result3);
        assertEquals(expect4, result4);
        assertEquals(expect5, result5);
    }

}