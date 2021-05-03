package com.demo.models;

import lombok.Data;

import javax.persistence.*;

@Table(name = "schedules")
@Entity
@Data
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String day;
    private Integer time;
}
