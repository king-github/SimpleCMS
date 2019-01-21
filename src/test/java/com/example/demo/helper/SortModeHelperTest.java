package com.example.demo.helper;

import com.example.demo.helper.order.OrderMode;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class SortModeHelperTest {

    private OrderModeHelper sortModeHelper;

    private OrderMode NONE;

    @Before
    public void before() {

        ArrayList<OrderMode> list = new ArrayList<>();
        NONE = new OrderMode("NONE", "none", "", false);
        list.add(NONE);
        list.add(new OrderMode("ID", "by id - ascending", "&sort=id,ASC", false));
        list.add(new OrderMode("ID_DESC", "by id - descending", "&sort=id,DESC", true));
        list.add(new OrderMode("DATE", "by date - ascending", "&sort=publicationDate,ASC", false));
        list.add(new OrderMode("DATE_DESC", "by date - descending", "&sort=publicationDate,DESC", true));

        sortModeHelper = new OrderModeHelper(list, NONE);
    }

    @Test
    public void givenOrderModeHelper_whenGetAllModes_thenReceiveAllModes() {

        List<OrderMode> allModes = sortModeHelper.getAllModes();
        assertEquals(5, allModes.size());
    }

    @Test
    public void givenParamString_whenFindModeByParamString_thenReceiveCorrectOrderMode() {

        OrderMode modeByParamString = sortModeHelper.findModeByParamString("&sort=publicationDate,ASC");

        assertEquals("&sort=publicationDate,ASC", modeByParamString.getParamString());
        assertEquals("DATE", modeByParamString.getId());
        assertEquals("by date - ascending", modeByParamString.getName());
        assertFalse(modeByParamString.isDesc());
    }

    @Test
    public void givenNoExistParamString_whenFindModeByParamString_thenReceiveDefaultOrderMode() {

        OrderMode result = sortModeHelper.findModeByParamString("&sort=invalidColumn,ASC");

        assertEquals(NONE, result);
    }

}