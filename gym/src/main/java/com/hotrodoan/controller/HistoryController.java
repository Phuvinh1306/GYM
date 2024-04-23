package com.hotrodoan.controller;

import java.util.List;

import com.hotrodoan.service.BookingService;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotrodoan.model.Member;
import com.hotrodoan.model.User;
import com.hotrodoan.repository.MemberRepository;
import com.hotrodoan.repository.UserRepository;
import com.hotrodoan.security.jwt.JwtProvider;
import com.hotrodoan.security.jwt.JwtTokenFilter;
import com.hotrodoan.service.HistoryService;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/histories")
public class HistoryController {
    @Autowired
    private HistoryService historyService;
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    private JwtProvider jwtProvider;

    @GetMapping("")
    public ResponseEntity<?> getAllHistories(HttpServletRequest request) {
        String jwt = jwtTokenFilter.getJwt(request);
        String username = jwtProvider.getUsernameFromToken(jwt);
        User user = userRepository.findByUsername(username).orElseThrow();
        Member member = memberRepository.findByUser(user);
        return new ResponseEntity<>(historyService.getAllHistories(member), HttpStatus.OK);
    }
}
