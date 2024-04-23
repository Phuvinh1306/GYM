package com.hotrodoan.service;

import com.hotrodoan.model.Booking;
import com.hotrodoan.model.WorkoutSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WorkoutSessionService {
    WorkoutSession updateWorkoutSession(WorkoutSession workoutSession, Long id);
    WorkoutSession getWorkoutSession(Long id);
    Page<WorkoutSession> getAllWorkoutSession(Pageable pageable);

    WorkoutSession getWorkoutSessionByBooking(Booking booking);
}
