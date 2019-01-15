package com.example.demo.entity;

import javax.persistence.*;
import java.util.*;

@Entity
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String label;

    @ManyToMany(mappedBy = "privileges")
    private Set<Role> roles = new HashSet<>();

    public Privilege() {
    }

    public Privilege(String name) {
        this.name = name;
    }

    public Privilege(String name, String label) {
        this.name = name;
        this.label = label;
    }

    public Privilege(Long id, String name, String label, Set<Role> roles) {
        this.id = id;
        this.name = name;
        this.label = label;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Privilege)) return false;
        Privilege privilege = (Privilege) o;
        return id != null && id.equals(privilege.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
