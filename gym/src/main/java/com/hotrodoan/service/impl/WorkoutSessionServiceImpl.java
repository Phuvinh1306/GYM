package com.hotrodoan.service.impl;

import com.hotrodoan.model.Booking;
import com.hotrodoan.model.WorkoutSession;
import com.hotrodoan.repository.WorkoutSessionRepository;
import com.hotrodoan.service.WorkoutSessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WorkoutSessionServiceImpl implements WorkoutSessionService {
    @Autowired
    private WorkoutSessionRepository workoutSessionRepository;

    @Override
    public WorkoutSession updateWorkoutSession(WorkoutSession workoutSession, Long id) {
        return workoutSessionRepository.findById(id).map(ws -> {
            ws.setDuration(workoutSession.getDuration());
            ws.setExercise(workoutSession.getExercise());
            ws.setGoal(workoutSession.getGoal());
            return workoutSessionRepository.save(ws);
        }).orElseThrow(()-> new RuntimeException("WorkoutSession not found"));
    }

    @Override
    public WorkoutSession getWorkoutSession(Long id) {
        return workoutSessionRepository.findById(id).orElseThrow(()-> new RuntimeException("WorkoutSession not found"));
    }

    @Override
    public Page<WorkoutSession> getAllWorkoutSession(Pageable pageable) {
        return workoutSessionRepository.findAll(pageable);
    }

    @Override
    public WorkoutSession getWorkoutSessionByBooking(Booking booking) {
        return workoutSessionRepository.findByBooking(booking);
    }
}
