package com.demo.models;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Table(name = "electives")
@Entity
@Data
public class Elective {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User instructor;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "elective_schedules",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = {@JoinColumn(name = "schedule_id")}
    )
    private Set<Schedule> schedules = new HashSet<>();

    private Integer pupilLimit;
}
