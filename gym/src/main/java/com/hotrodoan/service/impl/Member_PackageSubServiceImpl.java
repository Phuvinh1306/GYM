package com.hotrodoan.service.impl;

import com.hotrodoan.model.Member_PackageSub;
import com.hotrodoan.model.Package;
import com.hotrodoan.repository.Member_PackageSubRepository;
import com.hotrodoan.service.Member_PackageSubService;
import com.hotrodoan.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;

@Service
public class Member_PackageSubServiceImpl implements Member_PackageSubService {
    @Autowired
    private Member_PackageSubRepository member_PackageSubRepository;
    @Autowired
    private PackageService packageService;

    @Override
    public Member_PackageSub createMember_PackageSub(Member_PackageSub member_packageSub) {
        Date startDate = member_packageSub.getStartDate();
        Package pack = member_packageSub.getPack();
        Long packId = pack.getId();
        Package pack1 = packageService.getPackage(packId);
        int duration = pack1.getDuration() * member_packageSub.getQuantity();
        int totalPrice = pack1.getPrice() * member_packageSub.getQuantity();
        member_packageSub.setAmount(totalPrice);
        LocalDate localStartDate = startDate.toLocalDate();
        LocalDate localEndDate = localStartDate.plusDays(duration);
        Date endDate = Date.valueOf(localEndDate);
        member_packageSub.setEndDate(endDate);
        return member_PackageSubRepository.save(member_packageSub);
    }

    @Override
    public Member_PackageSub updateMember_PackageSub(Member_PackageSub member_packageSub, Long id) {
        return member_PackageSubRepository.findById(id).map(re -> {
            re.setMember(member_packageSub.getMember());
            re.setPack(member_packageSub.getPack());
            re.setStartDate(member_packageSub.getStartDate());
            re.setEndDate(member_packageSub.getEndDate());
            re.setAmount(member_packageSub.getAmount());
            return member_PackageSubRepository.save(re);
        }).orElseGet(() -> member_PackageSubRepository.save(member_packageSub));
    }

    @Override
    public Member_PackageSub getMember_PackageSub(Long id) {
        return member_PackageSubRepository.findById(id).orElseThrow(() -> new RuntimeException("Member package sub not found"));
    }

    @Override
    public void deleteMember_PackageSub(Long id) {
        member_PackageSubRepository.deleteById(id);
    }
}
