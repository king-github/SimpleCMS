package com.example.demo.entity;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.*;

@NamedEntityGraphs({
        @NamedEntityGraph(name = "User.fetchAll",
                attributeNodes = @NamedAttributeNode(value = "roles", subgraph = "roles.privileges"),
                subgraphs = @NamedSubgraph(name = "roles.privileges",
                        attributeNodes = @NamedAttributeNode( value = "privileges")))
})


@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    public enum UserStatus {
        UNDEFINED, NEW, ACTIVE, INACTIVE;

        public static List<UserStatus> getAllStatuses() {
            return Arrays.asList(UserStatus.values());
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Size(min=4, max=32)
    @Column(unique=true, nullable=false)
    private String username;

    @Email
    @Column(unique=true, nullable=false)
    private String email;

    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.UNDEFINED;

    private String hashedPassword;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();



    public User() {
    }

    public User(String username, String email, UserStatus status, String hashedPassword) {
        this.username = username;
        this.email = email;
        this.status = status;
        this.hashedPassword = hashedPassword;
    }

    public User(Long id, String username, String email, UserStatus status, String hashedPassword) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.status = status;
        this.hashedPassword = hashedPassword;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRoles(Collection<Role> rolesCollection) {

        for(Role role : rolesCollection) {
            roles.add(role);
            role.getUsers().add(this);
        }
    }

    public void removeRoles(Collection<Role> rolesCollection) {

        Iterator<Role> iterator = rolesCollection.iterator();
        while( iterator.hasNext()) {

            Role role = iterator.next();
            role.getUsers().remove(this);
            iterator.remove();
        }
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
