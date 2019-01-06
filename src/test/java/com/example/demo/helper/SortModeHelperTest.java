package com.example.demo.helper;

import com.example.demo.helper.order.OrderMode;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;


import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SortModeHelperTest {

    private OrderModeHelper sortModeHelper;

    @Before
    public void before() {

        ArrayList<OrderMode> list = new ArrayList<>();
        final OrderMode NONE = new OrderMode("NONE", "none", "", false);
        list.add(NONE);
        list.add(new OrderMode("ID", "by id - ascending", "&sort=id,ASC", false));
        list.add(new OrderMode("ID_DESC", "by id - descending", "&sort=id,DESC", true));
        list.add(new OrderMode("DATE", "by date - ascending", "&sort=publicationDate,ASC", false));
        list.add(new OrderMode("DATE_DESC", "by date - descending", "&sort=publicationDate,DESC", true));

        sortModeHelper = new OrderModeHelper(list, NONE);
    }

    @Test
    public void getAllModes() {
        List<OrderMode> allModes = sortModeHelper.getAllModes();
        assertEquals(5, allModes.size());
    }

    @Test
    public void getParamString() {
        // DATE("date ascending", "&sort=date,ASC")
        OrderMode modeByParamString = sortModeHelper.findModeByParamString("&sort=publicationDate,ASC");
        assertEquals("&sort=publicationDate,ASC", modeByParamString.getParamString());
    }

    @Test
    public void findModeByParamString() {
        // DATE("date ascending", "&sort=date,ASC")
        OrderMode modeByParamString = sortModeHelper.findModeByParamString("&sort=publicationDate,ASC");
        assertNotNull(modeByParamString);
    }

}