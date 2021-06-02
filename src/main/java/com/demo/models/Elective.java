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

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private Type type;

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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "elective_pupils",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<User> pupils = new HashSet<>();

    private Integer pupilLimit;

    public Boolean hasPupil(User pupil) {
        for (User user: pupils) {
            if (user.getId().equals(pupil.getId()))
                return true;
        }
        return false;
    }
}
