package com.demo.controllers;

import com.demo.repository.RoleRepository;
import com.demo.repository.TypeRepository;
import com.demo.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class MainController {
    private final TypeRepository typeRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public MainController(TypeRepository typeRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.typeRepository = typeRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("types", typeRepository.findAll());
        model.addAttribute("instructors", userRepository.findByRole((roleRepository.findByName("ROLE_INSTRUCTOR"))));
        return "index";
    }
}
