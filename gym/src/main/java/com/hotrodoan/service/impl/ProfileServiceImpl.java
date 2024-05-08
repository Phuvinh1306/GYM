package com.hotrodoan.service.impl;

import com.hotrodoan.model.Member;
import com.hotrodoan.model.User;
import com.hotrodoan.repository.MemberRepository;
import com.hotrodoan.repository.UserRepository;
import com.hotrodoan.service.MemberService;
import com.hotrodoan.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberService memberService;

    @Override
    public Optional<User> getProfile(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Member updateProfile(Member member, Long id) {
        return memberService.updateMember(member, id);
    }

    @Override
    public User changePassword(User user, Long id) {
        return userRepository.findById(id).map(us -> {
//            us.setPassword(passwordEncoder.encode(password));
//            us.setName(user.getName());
//            us.setUsername(user.getUsername());
            us.setPassword(passwordEncoder.encode(user.getPassword()));
//            us.setEmail(user.getEmail());
//            us.setAvatar(user.getAvatar());
            return userRepository.save(us);
        }).orElse(null);
    }

    @Override
    public void deleteProfile(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Member getProfileMember(User user) {
        return memberService.getMemberByUser(user);
    }
}
