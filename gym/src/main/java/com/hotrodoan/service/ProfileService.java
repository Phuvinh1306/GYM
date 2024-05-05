package com.hotrodoan.service;

import com.hotrodoan.model.User;

import java.util.Optional;

public interface ProfileService {
    Optional<User> getProfile(String username);
    User updateProfile(User user, Long id);
    User changePassword(User user, Long id);
    void deleteProfile(Long id);
}
