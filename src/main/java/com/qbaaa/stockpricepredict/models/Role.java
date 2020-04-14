package com.qbaaa.stockpricepredict.models;

import javax.persistence.*;

@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private ERole role;


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
}
