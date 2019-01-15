package com.example.demo.entity;



import javax.persistence.*;
import java.util.*;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String label;

    @ManyToMany(mappedBy = "roles")
    private Collection<User> users = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_privilege",
            joinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "privilege_id", referencedColumnName = "id"))
    private Set<Privilege> privileges = new HashSet<>();

    public Role() {
    }

    public Role(String name, Privilege... privilegeCollection) {
        this.name = name;
        addPrivileges(privilegeCollection);
    }

    public Role(String name, String label, Privilege... privilegeCollection) {
        this(name, privilegeCollection);
        this.label = label;
    }

    public Role(Long id, String name, String label, Collection<User> users, Set<Privilege> privileges) {
        this.id = id;
        this.name = name;
        this.label = label;
        this.users = users;
        this.privileges = privileges;
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

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Collection<Privilege> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(Set<Privilege> privileges) {
        this.privileges = privileges;
    }

    public void addPrivileges(Privilege... privilegesCollection){

        for (Privilege privilege :privilegesCollection) {
            privileges.add(privilege);
            privilege.getRoles().add(this);
        }
    }

    public void removePrivileges(Privilege... privilegesCollection){

        for (Privilege privilege :privilegesCollection) {
            privileges.remove(privilege);
            privilege.getRoles().remove(this);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        Role role = (Role) o;
        return id != null && id.equals(role.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
