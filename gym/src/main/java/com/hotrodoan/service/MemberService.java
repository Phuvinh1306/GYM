package com.hotrodoan.service;

import com.hotrodoan.dto.request.MemberDTO;
import com.hotrodoan.model.GymBranch;
import com.hotrodoan.model.Member;
import com.hotrodoan.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberService {
    Member addMember(Member member);
    Member addMember(MemberDTO memberDTO);
    List<Member> getAllMember();
    Member getMember(Long id);
    MemberDTO getMemberDTO(Long id);
    void deleteMember(Long id);
    Member updateMember(Member member, Long id);
    Member updateMember(MemberDTO memberDTO, Long id);
    Member getMemberByUser(User user);
    Page<Member> getAllMember(Pageable pageable);
    Page<Member> findMembersByAddressContaining(String address, Pageable pageable);
    int countMemberByGymBranch(GymBranch gymBranch);
}
