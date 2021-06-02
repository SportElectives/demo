package com.demo.models;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Table(name = "cancelled_days")
@Entity
@Data
public class CancelledDays {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String month;
    private Integer day;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "cancelled_days_schedules",
            joinColumns = @JoinColumn(name = "id"),
            inverseJoinColumns = {@JoinColumn(name = "schedule_id")}
    )
    private Set<Schedule> schedules = new HashSet<>();

    public boolean containSchedule(Long id) {
        for (Schedule schedule : schedules) {
            if (schedule.getId().equals(id)) return true;
        }
        return false;
    }
}
