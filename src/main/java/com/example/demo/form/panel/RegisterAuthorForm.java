package com.example.demo.form.panel;

import com.example.demo.validator.FieldsValueMatch;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@FieldsValueMatch.List({
        @FieldsValueMatch(
                field = "password",
                fieldMatch = "confirmPassword",
                message = "Passwords do not match!"
)})
public class RegisterAuthorForm {

    @Size(min=4, max=32)
    @Pattern(regexp="[a-zA-Z]\\w*", message = "starts with letter and must contain only letters and numbers")
    private String username;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Size(min=2, max=32)
    private String firstname;

    @NotEmpty
    @Size(min=2, max=32)
    private String lastname;

    @Size(min=4, max=32)
    private String password;

    @Size(min=4, max=32)
    private String confirmPassword;

    private String submit;

    public RegisterAuthorForm() {
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

    public String getSubmit() {
        return submit;
    }

    public void setSubmit(String submit) {
        this.submit = submit;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

}
