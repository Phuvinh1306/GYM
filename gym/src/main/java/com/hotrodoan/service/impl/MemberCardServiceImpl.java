package com.hotrodoan.service.impl;

import com.hotrodoan.model.MemberCard;
import com.hotrodoan.repository.MemberCardRepository;
import com.hotrodoan.service.MemberCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberCardServiceImpl implements MemberCardService {
    @Autowired
    private MemberCardRepository memberCardRepository;
    @Override
    public List<MemberCard> getAllMemberCard() {
        return memberCardRepository.findAll();
    }

    @Override
    public MemberCard addMemberCard(MemberCard memberCard) {
        return memberCardRepository.save(memberCard);
    }

    @Override
    public MemberCard updateMemberCard(MemberCard memberCard, Long id) {
        return memberCardRepository.findById(id).map(mc -> {
            mc.setName(memberCard.getName());
            mc.setStartDay(memberCard.getStartDay());
            mc.setEndDay(memberCard.getEndDay());
            return memberCardRepository.save(mc);
        }).orElseThrow(() -> new RuntimeException("Member card not found"));
    }

    @Override
    public void deleteMemberCard(Long id) {
        if (!memberCardRepository.existsById(id)) {
            throw new RuntimeException("Member card not found");
        }
        memberCardRepository.deleteById(id);
    }

    @Override
    public MemberCard getMemberCard(Long id) {
        return memberCardRepository.findById(id).orElseThrow(() -> new RuntimeException("Member card not found"));
    }

    @Override
    public Page<MemberCard> getAllMemberCard(Pageable pageable) {
        return memberCardRepository.findAll(pageable);
    }
}
