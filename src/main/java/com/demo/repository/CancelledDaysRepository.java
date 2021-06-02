package com.demo.repository;

import com.demo.models.CancelledDays;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CancelledDaysRepository extends JpaRepository<CancelledDays, Long> {
    Optional<CancelledDays> findByMonthAndDay(String month, Integer day);
}
