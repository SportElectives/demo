package com.demo.controllers;

import com.demo.models.User;
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
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @GetMapping("/admin/register")
    public String registration(Model model) {
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/admin/register")
    public String registration(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return "redirect:/";
    }

    @GetMapping("/pupil/profile")
    public String pupilProfile(Model model, Authentication authentication) {
        model.addAttribute("user", userRepository.findByIin(authentication.getName()));
        return "pupilProfile";
    }

    @GetMapping("/instructor/profile")
    public String instructorProfile(Model model, Authentication authentication) {
        model.addAttribute("user", userRepository.findByIin(authentication.getName()));
        return "instructorProfile";
    }
}
