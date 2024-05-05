package com.hotrodoan.service.impl;

import com.hotrodoan.model.User;
import com.hotrodoan.repository.UserRepository;
import com.hotrodoan.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileServiceImpl implements ProfileService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> getProfile(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User updateProfile(User user, Long id) {
        return userRepository.findById(id).map(us -> {
            us.setName(user.getName());
            us.setEmail(user.getEmail());
//            us.setPhone(user.getPhone());
//            us.setAddress(user.getAddress());
//            us.setAvatar(user.getAvatar());
            return userRepository.save(us);
        }).orElse(null);
    }

}
