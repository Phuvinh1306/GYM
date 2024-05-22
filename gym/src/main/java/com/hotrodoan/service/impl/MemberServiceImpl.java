package com.hotrodoan.service.impl;

import com.hotrodoan.exception.NotFoundMemberException;
import com.hotrodoan.model.GymBranch;
import com.hotrodoan.model.Member;
import com.hotrodoan.model.Package;
import com.hotrodoan.model.User;
import com.hotrodoan.repository.MemberRepository;
import com.hotrodoan.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberRepository memberRepository;

    @Override
    public Member addMember(Member member) {
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
    public void deleteMember(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new NotFoundMemberException("Không tìm thấy thành viên");
        }
        memberRepository.deleteById(id);
    }

    @Override
    public Member updateMember(Member member, Long id) {
        return memberRepository.findById(id).map(mb -> {
            mb.setName(member.getName());
            mb.setPhone(member.getPhone());
            mb.setCccd(member.getCccd());
            mb.setSex(member.getSex());
            mb.setUser(member.getUser());
            mb.setGymBranch(member.getGymBranch());
//            Set<Package> packages = new HashSet<>(member.getPackages());
//            mb.setPackages(packages);
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
    public Page<Member> findMembersByNameContaining(String name, Pageable pageable) {
        return memberRepository.findByNameContaining(name, pageable);
    }

//    @Override
//    public int countByGymBranch(GymBranch gymBranch) {
//        return memberRepository.countByGymBranch(gymBranch);
//    }

}
