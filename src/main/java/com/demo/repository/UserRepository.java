package com.demo.repository;

import com.demo.models.Role;
import com.demo.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByIin(String iin);
    List<User> findByRole(Role role);
}
