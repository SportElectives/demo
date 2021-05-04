package com.demo.controllers;

import com.demo.repository.ScheduleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/timeline")
public class CalendarController {
    private final ScheduleRepository scheduleRepository;

    public CalendarController(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public void getEvents() {

    }
}
