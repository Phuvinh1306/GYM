package com.hotrodoan.repository;

import com.hotrodoan.model.Booking;
import com.hotrodoan.model.WorkoutSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, Long> {
    Page<WorkoutSession> findAll(Pageable pageable);
    WorkoutSession findByBooking(Booking booking);
}
