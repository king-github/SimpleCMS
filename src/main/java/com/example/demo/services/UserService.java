package com.example.demo.services;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Page<User> getAllUsers(Pageable pageable);

    List<Role> getAllRoles();

    List<Role> getRoleByIds(Iterable<Long> ids);

    Optional<User> findUserById(Long id);

    User save(User user);
}
