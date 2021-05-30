package com.demo.controllers;

import com.demo.models.Elective;
import com.demo.models.User;
import com.demo.repository.ElectiveRepository;
import com.demo.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/calendar")
public class CalendarController {
    private final ElectiveRepository electiveRepository;
    private final UserRepository userRepository;

    public CalendarController(ElectiveRepository electiveRepository, UserRepository userRepository) {
        this.electiveRepository = electiveRepository;
        this.userRepository = userRepository;
    }


    @GetMapping("/")
    public String showElective(Model model, Authentication authentication) {
        User user = userRepository.findByIin(authentication.getName());
        List<Elective> myElectives = new ArrayList<>();
        List<Elective> allElectives = electiveRepository.findAll();
        if (user.getRole().getName().equals("ROLE_PUPIL")) {
            for (Elective elective : allElectives) {
                if (elective.hasPupil(user)) {
                    myElectives.add(elective);
                }
            }
        } else if (user.getRole().getName().equals("ROLE_INSTRUCTOR")) {
            for (Elective elective : allElectives) {
                if (elective.getInstructor().equals(user)) {
                    myElectives.add(elective);
                }
            }
        }
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
        c.set(Calendar.DAY_OF_MONTH, 0);
        Date lastDay = c.getTime();
        c.set(Calendar.MONTH, c.get(Calendar.MONTH));
        c.set(Calendar.DAY_OF_MONTH, 0);
        Date prevLastDay = c.getTime();
        int prevLastDayIndex = prevLastDay.getDay();
        int lastDayIndex = lastDay.getDay();
        int nextDays = 7 - lastDayIndex;

        model.addAttribute("lastDay", lastDay.getDate());
        model.addAttribute("prevLastDay", prevLastDay.getDate());
        model.addAttribute("prevLastDayIndex", prevLastDayIndex);
        model.addAttribute("lastDayIndex", lastDay.getDay());
        model.addAttribute("nextDays", nextDays);
        model.addAttribute("month", LocalDate.now().getMonth());
        model.addAttribute("day", LocalDate.now().getDayOfMonth());
        model.addAttribute("weekDays", DayOfWeek.values());
        String[] daysOfWeek = new String[] {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        model.addAttribute("daysOfWeek", daysOfWeek);
        model.addAttribute("electives", myElectives);
        return "calendar";
    }
}
