package com.example.demo.helper;

import org.hamcrest.CoreMatchers;
import org.junit.Test;


import static org.junit.Assert.*;

public class SortModeHelperTest {

    @Test
    public void getAllModes() {

        SortModeHelper sortModeHelper = new SortModeHelper();
        SortModeHelper.OrderMode[] allModes = sortModeHelper.getAllModes();

        assertEquals(allModes.length, 9);
    }

    @Test
    public void getName() {

        SortModeHelper sortModeHelper = new SortModeHelper();
        assertThat(SortModeHelper.OrderMode.DATE.getName(), CoreMatchers.containsString("date"));
        assertThat(SortModeHelper.OrderMode.DATE.getName(), CoreMatchers.containsString("ascending"));

    }

    @Test
    public void getParamString() {
        // DATE("date ascending", "&sort=date,ASC")
        SortModeHelper sortModeHelper = new SortModeHelper();
        assertEquals("&sort=publicationDate,ASC", SortModeHelper.OrderMode.DATE.getParamString());

    }

    @Test
    public void findModeByParamString() {
        // DATE("date ascending", "&sort=date,ASC")
        SortModeHelper sortModeHelper = new SortModeHelper();
        String paramString = SortModeHelper.OrderMode.DATE.getParamString();
        assertEquals(SortModeHelper.OrderMode.DATE, sortModeHelper.findModeByParamString(paramString));

    }

}