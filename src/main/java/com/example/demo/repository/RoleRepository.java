package com.example.demo.repository;

import com.example.demo.entity.Role;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @EntityGraph("Role.fetchAll")
    List<Role> findAll();

    @EntityGraph("Role.fetchAll")
    List<Role> findAllById(Iterable<Long> ids);
}
