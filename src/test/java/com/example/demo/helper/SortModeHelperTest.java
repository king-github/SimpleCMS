package com.example.demo.helper;

import org.hamcrest.CoreMatchers;
import org.junit.Test;


import java.util.List;

import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.Matchers.isIn;
import static org.junit.Assert.*;

public class SortModeHelperTest {

    @Test
    public void getAllModes() {

        SortModeHelper sortModeHelper = new SortModeHelper();
        List<SortModeHelper.OrderMode> allModes = sortModeHelper.getAllModes();

        assertEquals(13, allModes.size());
    }

    @Test
    public void getShortListOfModes() {

        SortModeHelper sortModeHelper = new SortModeHelper();
        List<SortModeHelper.OrderMode> modes = sortModeHelper.getShortListOfModes();

        assertEquals(9, modes.size());
        assertThat(SortModeHelper.OrderMode.DATE, isIn(modes));
        assertThat(SortModeHelper.OrderMode.DATE_DESC, isIn(modes));
        assertThat(SortModeHelper.OrderMode.ID, not(isIn(modes)));
        assertThat(SortModeHelper.OrderMode.ID_DESC, not((isIn(modes))));
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