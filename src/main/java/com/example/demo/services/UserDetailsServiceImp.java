package com.example.demo.services;

import com.example.demo.entity.Privilege;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("userDetailsService")
public class UserDetailsServiceImp implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        System.err.println("username:"+ username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));

        System.err.println("username:"+ user.getUsername());

        org.springframework.security.core.userdetails.User.UserBuilder builder = null;

        builder = org.springframework.security.core.userdetails.User.withUsername(user.getUsername());
        builder.disabled(false);
        builder.password(user.getHashedPassword());

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().stream().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
            role.getPrivileges().stream()
                    .map(Privilege::getName)
                    .map(SimpleGrantedAuthority::new)
                    .forEach(authority -> authorities.add(authority));
        });

        builder.authorities(authorities);

        return builder.build();
    }
}