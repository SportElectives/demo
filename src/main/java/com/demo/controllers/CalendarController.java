package com.demo.controllers;

import com.demo.models.Elective;
import com.demo.models.Schedule;
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
        Set<String> uniqueDays = new HashSet<>();
        if (user.getRole().getName().equals("ROLE_PUPIL")) {
            for (Elective elective : allElectives) {
                if (elective.hasPupil(user)) {
                    myElectives.add(elective);
                    for (Schedule schedule : elective.getSchedules()) {
                        uniqueDays.add(schedule.getDay());
                    }
                }
            }
        } else if (user.getRole().getName().equals("ROLE_INSTRUCTOR")) {
            for (Elective elective : allElectives) {
                if (elective.getInstructor().equals(user)) {
                    myElectives.add(elective);
                    for (Schedule schedule : elective.getSchedules()) {
                        uniqueDays.add(schedule.getDay());
                    }
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
        String[] months = new String[]{"Қаңтар", "Ақпан", "Наурыз", "Сәуір", "Мамыр", "Маусым", "Шілде",
                "Тамыз", "Қыркүйек", "Қазан", "Қараша", "Желтоқсан"};

        model.addAttribute("lastDay", lastDay.getDate());
        model.addAttribute("prevLastDay", prevLastDay.getDate());
        model.addAttribute("prevLastDayIndex", prevLastDayIndex);
        model.addAttribute("lastDayIndex", lastDay.getDay());
        model.addAttribute("nextDays", nextDays);
        model.addAttribute("month", months[LocalDate.now().getMonth().getValue()-1]);
        model.addAttribute("day", LocalDate.now().getDayOfMonth());
        String[] weekDays = new String[]{"Дүйсенбі", "Сейсенбі", "Сәрсенбі", "Бейсенбі", "Жұма", "Сенбі", "Жексенбі"};
        model.addAttribute("weekDays", weekDays);
        String[] daysOfWeek = new String[]{"Жексенбі", "Дүйсенбі", "Сейсенбі", "Сәрсенбі", "Бейсенбі", "Жұма", "Сенбі"};
        model.addAttribute("daysOfWeek", daysOfWeek);
        model.addAttribute("electives", myElectives);
        model.addAttribute("uniqueDays", uniqueDays);
        return "calendar";
    }
}
