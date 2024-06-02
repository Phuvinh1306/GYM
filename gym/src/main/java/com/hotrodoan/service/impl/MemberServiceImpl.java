package com.hotrodoan.service.impl;

import com.hotrodoan.dto.request.MemberDTO;
import com.hotrodoan.exception.NotFoundMemberException;
import com.hotrodoan.model.GymBranch;
import com.hotrodoan.model.Member;
import com.hotrodoan.model.Package;
import com.hotrodoan.model.User;
import com.hotrodoan.repository.MemberRepository;
import com.hotrodoan.service.MemberService;
import com.hotrodoan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private UserService userService;

    @Override
    public Member addMember(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public Member addMember(MemberDTO memberDTO) {
        User user = new User();
        Member member = new Member();
        user.setName(memberDTO.getName());
        user.setUsername(memberDTO.getUsername());
        user.setPassword(memberDTO.getPassword());
        user.setEmail(memberDTO.getEmail());
        user.setEnabled(true);
        User newUser = userService.save(user);
        member.setUser(newUser);
        member.setPhone(memberDTO.getPhone());
        member.setAddress(memberDTO.getAddress());
        member.setCccd(memberDTO.getCccd());
        member.setSex(memberDTO.getSex());
        member.setGymBranch(memberDTO.getGymBranch());
        member.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        return memberRepository.save(member);
    }

    @Override
    public List<Member> getAllMember() {
        return memberRepository.findAll();
    }

    @Override
    public Member getMember(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new NotFoundMemberException("Không tìm thấy thành viên"));
    }

    @Override
    public MemberDTO getMemberDTO(Long id) {
        MemberDTO memberDTO = new MemberDTO();
        Member member = memberRepository.findById(id).orElseThrow(() -> new NotFoundMemberException("Không tìm thấy thành viên"));
        User user = member.getUser();
        memberDTO.setName(user.getName());
        memberDTO.setUsername(user.getUsername());
        memberDTO.setEmail(user.getEmail());
        memberDTO.setPhone(member.getPhone());
        memberDTO.setAddress(member.getAddress());
        memberDTO.setCccd(member.getCccd());
        memberDTO.setSex(member.getSex());
        memberDTO.setGymBranch(member.getGymBranch());
        return memberDTO;
    }

    @Override
    public void deleteMember(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new NotFoundMemberException("Không tìm thấy thành viên");
        }
        memberRepository.deleteById(id);
    }

    @Override
    public Member updateMember(Member member, Long id) {
        return memberRepository.findById(id).map(mb -> {
            mb.setPhone(member.getPhone());
            mb.setCccd(member.getCccd());
            mb.setSex(member.getSex());
            mb.setUser(member.getUser());
            mb.setGymBranch(member.getGymBranch());
            mb.setAddress(member.getAddress());
//            Set<Package> packages = new HashSet<>(member.getPackages());
//            mb.setPackages(packages);
            return memberRepository.save(mb);
        }).orElseThrow(() -> new NotFoundMemberException("Không tìm thấy thành viên"));
    }

    @Override
    public Member updateMember(MemberDTO memberDTO, Long id) {
        return memberRepository.findById(id).map(mb -> {
            User user = memberRepository.findById(id).get().getUser();
            user.setName(memberDTO.getName());
            user.setUsername(memberDTO.getUsername());
            user.setEmail(memberDTO.getEmail());
            User updatedUser = userService.save(user);

            mb.setPhone(memberDTO.getPhone());
            mb.setCccd(memberDTO.getCccd());
            mb.setSex(memberDTO.getSex());
            mb.setUser(updatedUser);
            mb.setGymBranch(memberDTO.getGymBranch());
            mb.setAddress(memberDTO.getAddress());
            return memberRepository.save(mb);
        }).orElseThrow(() -> new NotFoundMemberException("Không tìm thấy thành viên"));
    }

    @Override
    public Member getMemberByUser(User user) {
        return memberRepository.findByUser(user);
    }

    @Override
    public Page<Member> getAllMember(Pageable pageable) {
        return memberRepository.findAll(pageable);
    }

    @Override
    public Page<Member> findMembersByAddressContaining(String address, Pageable pageable) {
        return memberRepository.findByAddressContaining(address, pageable);
    }

    @Override
    public int countMemberByGymBranch(GymBranch gymBranch) {
        return memberRepository.countByGymBranch(gymBranch);
    }

}
