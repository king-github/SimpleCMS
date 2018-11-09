package com.example.demo.dto;

public class AuthorWithQuantityDto {

    private Long id;

    private String firstname;

    private String lastname;

    private Long quantity;

    public AuthorWithQuantityDto() {
    }

    public AuthorWithQuantityDto(Long id, String firstname, String lastname, Long quantity) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }
}
