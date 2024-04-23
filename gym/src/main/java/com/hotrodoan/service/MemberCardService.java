package com.hotrodoan.service;

import com.hotrodoan.model.MemberCard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberCardService {
    List<MemberCard> getAllMemberCard();
    MemberCard addMemberCard(MemberCard memberCard);
    MemberCard updateMemberCard(MemberCard memberCard, Long id);
    void deleteMemberCard(Long id);
    MemberCard getMemberCard(Long id);

    Page<MemberCard> getAllMemberCard(Pageable pageable);
}
