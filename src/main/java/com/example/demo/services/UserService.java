package com.example.demo.services;

import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<User> getAllUsers(Pageable pageable);
}
