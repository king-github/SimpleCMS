package com.example.demo.repository;

import com.example.demo.entity.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {

    Optional<Privilege> findByName(String name);

    List<Privilege> findByNameIn(String... name);

}
