package com.demo.controllers;

import com.demo.models.*;
import com.demo.repository.CancelledDaysRepository;
import com.demo.repository.ElectiveRepository;
import com.demo.repository.ScheduleRepository;
import com.demo.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/elective")
public class ElectiveController {
    private final ElectiveRepository electiveRepository;
    private final ScheduleRepository scheduleRepository;
    private final CancelledDaysRepository cancelledDaysRepository;
    private final UserRepository userRepository;

    public ElectiveController(ElectiveRepository electiveRepository, ScheduleRepository scheduleRepository, CancelledDaysRepository cancelledDaysRepository, UserRepository userRepository) {
        this.electiveRepository = electiveRepository;
        this.scheduleRepository = scheduleRepository;
        this.cancelledDaysRepository = cancelledDaysRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public String showElective(@PathVariable("id") Long id, Model model) {
        Elective elective = electiveRepository.findById(id).orElse(null);
        model.addAttribute("elective", elective);
        if (elective != null) {
            User instructor = elective.getInstructor();
            Type type = elective.getType();
            model.addAttribute("instructor", instructor.getFullName());
            model.addAttribute("type", type.getName());
        }
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

    @PostMapping("/update/{id}/admin")
    public String update(@PathVariable("id") Long id, @RequestParam("pupilLimit") Integer pupilLimit) {
        Elective elective = electiveRepository.findById(id).orElse(null);
        if (elective != null && pupilLimit >= 0 && pupilLimit <= 40) {
            elective.setPupilLimit(pupilLimit);
            electiveRepository.save(elective);
        }
        return "redirect:/user/admin/profile";
    }

    @GetMapping("/join/{id}")
    public String joinElective(@PathVariable("id") Long id, Authentication authentication) {
        User pupil = userRepository.findByIin(authentication.getName());
        Elective elective = electiveRepository.findById(id).orElse(null);
        if (elective != null) {
            if (!elective.hasPupil(pupil) && (elective.getPupilLimit() - elective.getPupils().size()) > 0) {
                elective.getPupils().add(pupil);
                electiveRepository.save(elective);
            }
        }
        return "redirect:/user/pupil/profile";
    }

    @GetMapping("/page/{id}")
    public String showElectivePage(@PathVariable("id") Long id, Model model, Authentication authentication) {
        model.addAttribute("user", userRepository.findByIin(authentication.getName()));
        model.addAttribute("elective", electiveRepository.findById(id).orElse(null));
        return "electivePagePupil";
    }

    @GetMapping("/{id}/{day}/{schedule_id}")
    public String cancelDays(@PathVariable("id") Long id, @PathVariable("day") Integer day, @PathVariable("schedule_id") Long schedule_id) {
        String[] months = new String[]{"Қаңтар", "Ақпан", "Наурыз", "Сәуір", "Мамыр", "Маусым", "Шілде",
                "Тамыз", "Қыркүйек", "Қазан", "Қараша", "Желтоқсан"};
        Schedule schedule = scheduleRepository.findById(schedule_id).orElse(null);
        String currentMonth = months[LocalDate.now().getMonth().getValue()-1];
        Elective elective = electiveRepository.findById(id).orElse(null);
        CancelledDays cancelledDay = cancelledDaysRepository.findByMonthAndDay(currentMonth, day).orElse(new CancelledDays());
        cancelledDay.setDay(day);
        cancelledDay.setMonth(currentMonth);
        cancelledDay.getSchedules().add(schedule);
        cancelledDaysRepository.save(cancelledDay);
        assert elective != null;
        elective.getCancelledDays().add(cancelledDaysRepository.findByMonthAndDay(currentMonth, day).orElse(null));
        electiveRepository.save(elective);
        return "redirect:/user/instructor/profile";
    }
}
