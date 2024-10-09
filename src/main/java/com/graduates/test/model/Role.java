package com.graduates.test.model;

import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "roles")

public class Role {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer idRoles;

    private String name;

    @ManyToMany(mappedBy = "roles")
    private List<UserEntity> users = new ArrayList<>();

    // Getters and setters
}
