package com.hotrodoan.service;

import com.hotrodoan.model.Member;
import com.hotrodoan.model.User;

import java.util.Optional;

public interface ProfileService {
    Optional<User> getProfile(String username);
    Member updateProfile(Member member, Long id);
    User changePassword(User user, Long id);
    void deleteProfile(Long id);

    Member getProfileMember(User user);
}
