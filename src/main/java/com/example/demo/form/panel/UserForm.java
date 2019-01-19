package com.example.demo.form.panel;

import com.example.demo.entity.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class UserForm {

    public Long id;

    @Size(min=4, max=32)
    @Pattern(regexp="[a-zA-Z]\\w*", message = "starts with letter and must contain only letters and numbers")
    private String username;

    @NotEmpty
    @Email
    private String email;

    private User.UserStatus status;

    private List<Long> roleIds = new ArrayList<>();

    private String submit;

    public UserForm() {
    }

    public UserForm(Long id, String username, String email, User.UserStatus status, List<Long> roleIds) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.status = status;
        this.roleIds = roleIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User.UserStatus getStatus() {
        return status;
    }

    public void setStatus(User.UserStatus status) {
        this.status = status;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public String getSubmit() {
        return submit;
    }

    public void setSubmit(String submit) {
        this.submit = submit;
    }
}
