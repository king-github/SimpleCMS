package com.example.demo.helper;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class SortModeHelperTest {

    @Test
    public void getAllModes() {

        SortModeHelper sortModeHelper = new SortModeHelper();
        SortModeHelper.OrderMode[] allModes = sortModeHelper.getAllModes();

        SortModeHelper.OrderMode[] expected = { SortModeHelper.OrderMode.NONE,
                                                SortModeHelper.OrderMode.DATE,
                                                SortModeHelper.OrderMode.DATE_DESC };

        assertEquals(allModes.length, 7);
    }

    @Test
    public void getName() {
        // DATE("date ascending", "&sort=date,ASC")
        SortModeHelper sortModeHelper = new SortModeHelper();
        assertEquals("by date ascending", SortModeHelper.OrderMode.DATE.getName());

    }

    @Test
    public void getParamString() {
        // DATE("date ascending", "&sort=date,ASC")
        SortModeHelper sortModeHelper = new SortModeHelper();
        assertEquals("&sort=date,ASC", SortModeHelper.OrderMode.DATE.getParamString());

    }

    @Test
    public void findModeByParamString() {
        // DATE("date ascending", "&sort=date,ASC")
        SortModeHelper sortModeHelper = new SortModeHelper();
        assertEquals(SortModeHelper.OrderMode.DATE, sortModeHelper.findModeByParamString("&sort=date,ASC"));

    }

}