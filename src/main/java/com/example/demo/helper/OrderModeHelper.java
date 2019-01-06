package com.example.demo.helper;

import com.example.demo.helper.order.OrderMode;

import java.util.List;

public class OrderModeHelper {

    private List<OrderMode> orderModes;
    private OrderMode defaultOrder;

    public OrderModeHelper(List<OrderMode> orderModes) {
        this.orderModes = orderModes;
    }

    public OrderModeHelper(List<OrderMode> orderModes, OrderMode defaultOrder) {
        this.orderModes = orderModes;
        this.defaultOrder = defaultOrder;
    }

    public List<OrderMode> getAllModes() {

        return orderModes;
    }

    public OrderMode findModeByParamString(String param) {

        return orderModes.stream()
                .filter(orderMode -> orderMode.getParamString().equals(param))
                .findAny()
                .orElse(defaultOrder)
                ;
    }

}
