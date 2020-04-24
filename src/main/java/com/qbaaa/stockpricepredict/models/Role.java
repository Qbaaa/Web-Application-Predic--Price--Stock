package com.qbaaa.stockpricepredict.models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private ERole role;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    public Role() {
    }

    public Role(ERole role) {
        this.role = role;
    }

    public Integer getId() {
        return id;
    }

    public ERole getRole() {
        return role;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setRole(ERole role) {
        this.role = role;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
