package com.example.demo.form.panel;


import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Component
public class UserFormUserConverter {

    @Autowired
    private UserService userService;

    public User toUser(UserForm userForm, User user) {

        List<Role> roles = userService.getRoleByIds(userForm.getRoleIds());

        user.setUsername(userForm.getUsername());
        user.setEmail(userForm.getEmail());
        user.setStatus(userForm.getStatus());
        user.removeRoles(user.getRoles());
        user.addRoles(roles);
        return user;
    }

    public UserForm toUserForm(User user) {

        List<Long> ids = user.getRoles().stream().map(Role::getId).collect(Collectors.toList());

        return new UserForm(user.getId(),
                            user.getUsername(),
                            user.getEmail(),
                            user.getStatus(),
                            ids
        );
    }

    public Map<String, Object> toMap (UserForm userForm) {

        HashMap<String, Object> map = new HashMap<>();

        map.put("id", userForm.getId());
        map.put("username", userForm.getUsername());
        map.put("email", userForm.getEmail());
        map.put("status", userForm.getStatus());
        map.put("roleIds", userForm.getRoleIds());

        return map;
    }
}
