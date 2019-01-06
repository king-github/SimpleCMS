package com.example.demo.helper.order;

public class OrderMode {

    private String id;
    private String name;
    private String paramString;
    private boolean desc;

    public OrderMode(String id, String name, String paramString, boolean desc) {
        this.id = id;
        this.name = name;
        this.paramString = paramString;
        this.desc = desc;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isDesc() {
        return desc;
    }

    public String getParamString() {
        return paramString;
    }

}
