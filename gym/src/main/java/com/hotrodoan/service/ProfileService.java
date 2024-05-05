package com.hotrodoan.service;

import com.hotrodoan.model.User;

import java.util.Optional;

public interface ProfileService {
    Optional<User> getProfile(String username);
    User updateProfile(User user, Long id);
//    void changePassword(String username, String password);
//    void deleteProfile(String username);
}
