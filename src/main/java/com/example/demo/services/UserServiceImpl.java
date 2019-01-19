package com.example.demo.services;

import com.example.demo.entity.Role;
import com.example.demo.entity.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Page<User> getAllUsers(Pageable pageable){

        return userRepository.findAll(pageable);
    }

    @Override
    public List<Role> getAllRoles(){

        return roleRepository.findAll();
    }

    @Override
    public List<Role> getRoleByIds(Iterable<Long> ids) {

        return roleRepository.findAllById(ids);
    }

    @Override
    public Optional<User> findUserById(Long id) {

        return userRepository.findById(id);
    }

    @Override
    public User save(User user) {

        return userRepository.save(user);
    }

}
