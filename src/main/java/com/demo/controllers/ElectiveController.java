package com.demo.controllers;

import com.demo.models.Elective;
import com.demo.models.Schedule;
import com.demo.repository.ElectiveRepository;
import com.demo.repository.ScheduleRepository;
import com.demo.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/elective")
public class ElectiveController {
    private final ElectiveRepository electiveRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public ElectiveController(ElectiveRepository electiveRepository, ScheduleRepository scheduleRepository, UserRepository userRepository) {
        this.electiveRepository = electiveRepository;
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public String showElective(@PathVariable("id") Long id, Model model) {
        model.addAttribute("elective", electiveRepository.findById(id).orElse(null));
        return "electivePage";
    }

    @PostMapping("/update/{id}/instructor")
    public String update(@PathVariable("id") Long id, @RequestParam("day") String day,
                         @RequestParam("hour") String hour, @RequestParam("minute") String minute) {
        Elective elective = electiveRepository.findById(id).orElse(null);
        int time = Integer.parseInt(hour) * 60 + Integer.parseInt(minute);
        Schedule schedule = scheduleRepository.findByDayAndTime(day, time)
                .orElse(new Schedule());
        schedule.setDay(day);
        schedule.setTime(time);
        scheduleRepository.save(schedule);
        assert elective != null;
        elective.getSchedules().add(scheduleRepository.findByDayAndTime(day, time).orElse(null));
        electiveRepository.save(elective);
        return "redirect:/user/instructor/profile";
    }
}