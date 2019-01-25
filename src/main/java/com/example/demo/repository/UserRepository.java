package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph("User.fetchAll")
    Optional<User> findById(Long id);

    @EntityGraph("User.fetchAll")
    Optional<User> findByUsername(String username);

    @EntityGraph("User.fetchAll")
    Page<User> findAll(Pageable pageable);

}
