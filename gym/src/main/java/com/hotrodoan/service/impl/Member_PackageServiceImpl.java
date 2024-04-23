package com.hotrodoan.service.impl;

import com.hotrodoan.model.Member;
import com.hotrodoan.model.Member_Package;
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
        double totalPrice = pack1.getPrice() * member_package.getQuantity();
        member_package.setTotalPrice(totalPrice);
        LocalDate localStartDate = startDate.toLocalDate();
        LocalDate localEndDate = localStartDate.plusDays(duration);
        Date endDate = Date.valueOf(localEndDate);
        member_package.setEndDate(endDate);
//        Member_Package newMember_Package = member_packageRepository.save(member_package);

//        String paymentUrl = createPaymentUrl(newMember_Package.getTotalPrice());
//
//        redirectToPaymentPage(paymentUrl);

//        return newMember_Package;
        return member_packageRepository.save(member_package);
    }

//    private String createPaymentUrl(double amount) {
//        // Tạo các tham số cần thiết cho URL thanh toán
//        Map<String, String> paymentParams = new HashMap<>();
//        // Các tham số khác cần thiết cho URL thanh toán
//        paymentParams.put("amount", String.valueOf(amount)); // Số tiền cần thanh toán
//
//        // Xây dựng URL thanh toán từ các tham số
//        StringBuilder paymentUrlBuilder = new StringBuilder("URL_THANH_TOAN");
//        paymentUrlBuilder.append("?"); // Phần query bắt đầu
//
//        // Thêm các tham số vào URL thanh toán
//        paymentParams.forEach((key, value) -> {
//            try {
//                paymentUrlBuilder.append(key)
//                        .append("=")
//                        .append(URLEncoder.encode(value, StandardCharsets.UTF_8.toString()))
//                        .append("&");
//            } catch (IOException e) {
//                e.printStackTrace(); // Xử lý ngoại lệ nếu có
//            }
//        });
//
//        // Trả về URL thanh toán đã được xây dựng
//        return paymentUrlBuilder.toString();
//    }
//
//    private void redirectToPaymentPage(String paymentUrl) {
//        try {
//            httpServletResponse.sendRedirect(paymentUrl);
//        } catch (IOException e) {
//            e.printStackTrace(); // Xử lý ngoại lệ nếu có
//        }
//    }

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
}
