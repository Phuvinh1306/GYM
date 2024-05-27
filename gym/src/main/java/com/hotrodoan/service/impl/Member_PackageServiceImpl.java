package com.hotrodoan.service.impl;

import com.hotrodoan.model.Member;
import com.hotrodoan.model.Member_Package;
import com.hotrodoan.model.Member_PackageSub;
import com.hotrodoan.model.Package;
import com.hotrodoan.repository.Member_PackageRepository;
import com.hotrodoan.service.Member_PackageService;
import com.hotrodoan.service.PackageService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class Member_PackageServiceImpl implements Member_PackageService {
    @Autowired
    private Member_PackageRepository member_packageRepository;

    @Autowired
    private PackageService packageService;

    @Autowired
    private HttpServletResponse httpServletResponse;

    @Override
    public Page<Member_Package> getAllMember_Package(Pageable pageable) {
        return member_packageRepository.findAll(pageable);
    }

    @Override
    public Page<Member_Package> findMember_PackageByMember(Member member, Pageable pageable) {
        return member_packageRepository.findByMember(member, pageable);
    }

    @Override
    public Member_Package addMember_Package(Member_Package member_package) {
        Date startDate = member_package.getStartDate();
        Package pack = member_package.getPack();
        Long packId = pack.getId();
        Package pack1 = packageService.getPackage(packId);
        int duration = pack1.getDuration() * member_package.getQuantity();
        int totalPrice = pack1.getPrice() * member_package.getQuantity();
        member_package.setAmount(totalPrice);
        LocalDate localStartDate = startDate.toLocalDate();
        LocalDate localEndDate = localStartDate.plusDays(duration);
        Date endDate = Date.valueOf(localEndDate);
        member_package.setEndDate(endDate);
        return member_packageRepository.save(member_package);
//        Member_Package newMember_Package = member_packageRepository.save(member_package);

//        String paymentUrl = createPaymentUrl(newMember_Package.getTotalPrice());
//
//        redirectToPaymentPage(paymentUrl);

//        return newMember_Package;
    }


    @Override
    public Member_Package updateMember_Package(Member_Package member_package, Long id) {
        return member_packageRepository.findById(id).map(mp -> {
            mp.setMember(member_package.getMember());
            mp.setPack(member_package.getPack());
            mp.setStartDate(member_package.getStartDate());

            Date startDate = member_package.getStartDate();
            Package pack = member_package.getPack();
            Long packId = pack.getId();
            Package pack1 = packageService.getPackage(packId);
            int duration = pack1.getDuration();
            LocalDate localStartDate = startDate.toLocalDate();
            LocalDate localEndDate = localStartDate.plusDays(duration);
            Date endDate = Date.valueOf(localEndDate);
            mp.setEndDate(endDate);
            return member_packageRepository.save(mp);
        }).orElseThrow(() -> new RuntimeException("Không tìm thấy gói dịch vụ"));
    }

    @Override
    public void deleteMember_Package(Long id) {
        if (!member_packageRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy gói dịch vụ");
        }
        member_packageRepository.deleteById(id);
    }

    @Override
    public Member_Package getMember_Package(Long id) {
        return member_packageRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy gói dịch vụ"));
    }

    @Override
    public Member_Package createMember_PackageBySub(Member_PackageSub memberPackageSub) {
        Member_Package member_package = new Member_Package();
        member_package.setMember(memberPackageSub.getMember());
        member_package.setPack(memberPackageSub.getPack());
        member_package.setStartDate(memberPackageSub.getStartDate());
        member_package.setQuantity(memberPackageSub.getQuantity());
        member_package.setAmount(memberPackageSub.getAmount());
        member_package.setEndDate(memberPackageSub.getEndDate());
        return member_packageRepository.save(member_package);
    }

    @Override
    public boolean checkExistsByMember(Member member) {
        return member_packageRepository.existsByMember(member);
    }

    @Override
    public List<Member_Package> getTheExpriredMember_Package(Date today) {
        return member_packageRepository.findByEndDateBefore(today);
    }

    @Override
    public List<Member_Package> getMember_PackageByMember(Member member) {
        return member_packageRepository.findByMember(member);
    }
}
