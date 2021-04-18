package com.demo.models;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "RoleEntity")
@Table(name = "roles")
@Data
public class Role implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;
}
