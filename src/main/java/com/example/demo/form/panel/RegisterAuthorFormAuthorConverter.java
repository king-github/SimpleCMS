package com.example.demo.form.panel;


import com.example.demo.entity.Author;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class RegisterAuthorFormAuthorConverter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Author toAuthor(RegisterAuthorForm userForm) {

        //List<Role> roles = userService.getRoleByIds();

        return new Author(userForm.getUsername(),
                userForm.getEmail(),
                User.UserStatus.NEW,
                passwordEncoder.encode(userForm.getPassword()),
                userForm.getFirstname(),
                userForm.getLastname()
        );

    }

    // not required
    public RegisterAuthorForm toUserForm(Author author) {

        return null;
    }


    public Map<String, Object> toMap (RegisterAuthorForm userForm) {

        HashMap<String, Object> map = new HashMap<>();

        map.put("username", userForm.getUsername());
        map.put("email", userForm.getEmail());
        map.put("firstname", userForm.getFirstname());
        map.put("lastname", userForm.getLastname());
        map.put("password", userForm.getPassword());
        map.put("confirmPassword", userForm.getConfirmPassword());

        return map;
    }
}
