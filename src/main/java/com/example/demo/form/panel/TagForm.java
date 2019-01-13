package com.example.demo.form.panel;

import javax.validation.constraints.Size;

public class TagForm {

    private Long id;

    @Size(min=3, max=25)
    private String name;

    public TagForm() {
    }

    public TagForm(Long id, @Size(min = 3, max = 25) String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
