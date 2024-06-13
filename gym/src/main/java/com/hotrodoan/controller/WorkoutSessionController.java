package com.hotrodoan.controller;

import com.hotrodoan.model.*;
import com.hotrodoan.security.jwt.JwtProvider;
import com.hotrodoan.security.jwt.JwtTokenFilter;
import com.hotrodoan.service.BookingService;
import com.hotrodoan.service.MemberService;
import com.hotrodoan.service.UserService;
import com.hotrodoan.service.WorkoutSessionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/workout-sessions")
@CrossOrigin(origins = "*")
public class WorkoutSessionController {
    @Autowired
    private WorkoutSessionService workoutSessionService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    private UserService userService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/admin/all")
    public ResponseEntity<Page<WorkoutSession>> getAllWorkoutSession(@RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size,
                                                                     @RequestParam(defaultValue = "id") String sortBy,
                                                                     @RequestParam(defaultValue = "desc") String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sortBy)));
        return new ResponseEntity<>(workoutSessionService.getAllWorkoutSession(pageable), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<WorkoutSession> updateWorkoutSession(@RequestBody WorkoutSession workoutSession, @PathVariable Long id){
        return new ResponseEntity<>(workoutSessionService.updateWorkoutSession(workoutSession, id), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WorkoutSession> getWorkoutSession(@PathVariable("id") Long id) {
        return new ResponseEntity<>(workoutSessionService.getWorkoutSession(id), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<?> pageWorkoutSession(HttpServletRequest request, @RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "10") int size,
                                                @RequestParam(defaultValue = "id") String sortBy,
                                                @RequestParam(defaultValue = "desc") String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sortBy)));
        String jwt = jwtTokenFilter.getJwt(request);
        String username = jwtProvider.getUsernameFromToken(jwt);
        User user = userService.findByUsername(username).orElseThrow();
        Member member = memberService.getMemberByUser(user);
        Page<Booking> bookings = bookingService.findBookingByMemberId(member.getId(), pageable);

        if (bookings.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else {
            List<WorkoutSession> workoutSessions = new ArrayList<>();
            for (Booking booking : bookings) {
                workoutSessions.add(workoutSessionService.getWorkoutSessionByBooking(booking));
            }
            return new ResponseEntity<>(workoutSessions, HttpStatus.OK);
        }
    }
}
