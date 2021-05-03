package com.demo.repository;

import com.demo.models.Elective;
import com.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ElectiveRepository extends JpaRepository<Elective, Long> {
    List<Elective> findByInstructor(User user);
}
