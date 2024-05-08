package com.hotrodoan.controller;

import com.hotrodoan.dto.response.ResponseMessage;
import com.hotrodoan.model.Member;
import com.hotrodoan.model.User;
import com.hotrodoan.security.jwt.JwtProvider;
import com.hotrodoan.security.jwt.JwtTokenFilter;
import com.hotrodoan.service.ProfileService;
import com.hotrodoan.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/profile")
@CrossOrigin(origins = "*")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserService userService;

    @GetMapping("")
//    public ResponseEntity<?> getProfile(HttpServletRequest request) {
//        String jwt = jwtTokenFilter.getJwt(request);
//        String username = jwtProvider.getUsernameFromToken(jwt);
//        User user = userService.findByUsername(username).orElseThrow();
//        return new ResponseEntity<>(user, HttpStatus.OK);
//    }
    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        String jwt = jwtTokenFilter.getJwt(request);
        String username = jwtProvider.getUsernameFromToken(jwt);
        User user = userService.findByUsername(username).orElseThrow();
        Member member = profileService.getProfileMember(user);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(HttpServletRequest request, @RequestBody Member member) {
        String jwt = jwtTokenFilter.getJwt(request);
        String username = jwtProvider.getUsernameFromToken(jwt);
        User user = userService.findByUsername(username).orElseThrow();
        Member member1 = profileService.getProfileMember(user);
        return new ResponseEntity<>(profileService.updateProfile(member, member1.getId()), HttpStatus.OK);
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(HttpServletRequest request, @RequestBody User u) {
        String jwt = jwtTokenFilter.getJwt(request);
        String username = jwtProvider.getUsernameFromToken(jwt);
        User user1 = userService.findByUsername(username).orElseThrow();
        u.setName(user1.getName());
        u.setUsername(user1.getUsername());
        u.setEmail(user1.getEmail());
        u.setAvatar(user1.getAvatar());
//        User user = new User();
//        user.setPassword(password);
        return new ResponseEntity<>(profileService.changePassword(u, user1.getId()), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProfile(HttpServletRequest request) {
        String jwt = jwtTokenFilter.getJwt(request);
        String username = jwtProvider.getUsernameFromToken(jwt);
        User user = userService.findByUsername(username).orElseThrow();
        profileService.deleteProfile(user.getId());
        return new ResponseEntity<>(new ResponseMessage("deleted"), HttpStatus.OK);
    }
}
