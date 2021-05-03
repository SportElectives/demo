package com.demo.controllers;

import com.demo.models.Elective;
import com.demo.models.User;
import com.demo.repository.ElectiveRepository;
import com.demo.repository.RoleRepository;
import com.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ElectiveRepository electiveRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository, RoleRepository roleRepository, ElectiveRepository electiveRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.electiveRepository = electiveRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @GetMapping("/admin/register")
    public String userRegistration(Model model) {
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("user", new User());
        return "userRegistration";
    }

    @PostMapping("/admin/register")
    public String userRegistration(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/";
    }

    @GetMapping("/admin/elective/register")
    public String electiveRegistration(Model model) {
        model.addAttribute("elective", new Elective());
        model.addAttribute("instructors", userRepository.findByRole(roleRepository.findByName("ROLE_INSTRUCTOR")));
        return "electiveRegistration";
    }

    @PostMapping("/admin/elective/register")
    public String electiveRegistration(Elective elective) {
        electiveRepository.save(elective);
        return "redirect:/user/admin/profile";
    }

    @GetMapping("/admin/profile")
    public String adminProfile(Model model, Authentication authentication) {
        model.addAttribute("user", userRepository.findByIin(authentication.getName()));
        return "adminProfile";
    }

    @GetMapping("/pupil/profile")
    public String pupilProfile(Model model, Authentication authentication) {
        model.addAttribute("user", userRepository.findByIin(authentication.getName()));
        return "pupilProfile";
    }

    @GetMapping("/instructor/profile")
    public String instructorProfile(Model model, Authentication authentication) {
        User user = userRepository.findByIin(authentication.getName());
        model.addAttribute("user", user);
        model.addAttribute("electives", electiveRepository.findByInstructor(user));
        return "instructorProfile";
    }
}
