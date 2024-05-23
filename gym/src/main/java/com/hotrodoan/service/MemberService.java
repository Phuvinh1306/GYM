package com.hotrodoan.service;

import com.hotrodoan.model.GymBranch;
import com.hotrodoan.model.Member;
import com.hotrodoan.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberService {
    Member addMember(Member member);
    List<Member> getAllMember();
    Member getMember(Long id);
    void deleteMember(Long id);
    Member updateMember(Member member, Long id);

    Member getMemberByUser(User user);

    Page<Member> getAllMember(Pageable pageable);

    Page<Member> findMembersByNameContaining(String name, Pageable pageable);

    int countMemberByGymBranch(GymBranch gymBranch);
}
