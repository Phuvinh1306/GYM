package com.hotrodoan.controller;

import com.hotrodoan.model.Member;
import com.hotrodoan.model.Member_Package;
import com.hotrodoan.model.Package;
import com.hotrodoan.model.User;
import com.hotrodoan.security.jwt.JwtProvider;
import com.hotrodoan.security.jwt.JwtTokenFilter;
import com.hotrodoan.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/member_packages")
public class Member_PackageController {
    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    private UserService userService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private Member_PackageService member_packageService;

    @Autowired
    private PackageService packageService;

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private HttpSession httpSession;

    @GetMapping("/admin/all")
    public ResponseEntity<Page<Member_Package>> getAllMember_Package(@RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size,
                                                                     @RequestParam(defaultValue = "id") String sortBy,
                                                                     @RequestParam(defaultValue = "desc") String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sortBy)));
        return new ResponseEntity<>(member_packageService.getAllMember_Package(pageable), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<Page<Member_Package>> getMember_PackageByMember(HttpServletRequest request, @RequestParam(defaultValue = "0") int page,
                                                                          @RequestParam(defaultValue = "10") int size,
                                                                          @RequestParam(defaultValue = "id") String sortBy,
                                                                          @RequestParam(defaultValue = "desc") String order) {
        String jwt = jwtTokenFilter.getJwt(request);
        String username = jwtProvider.getUsernameFromToken(jwt);
        User user = userService.findByUsername(username).orElseThrow();
        Member member = memberService.getMemberByUser(user);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sortBy)));
        return new ResponseEntity<>(member_packageService.findMember_PackageByMember(member, pageable), HttpStatus.OK);
    }

    @PostMapping("/admin/add")
    public ResponseEntity<Member_Package> addMember_Package(@RequestBody Member_Package member_package) {
        return new ResponseEntity<>(member_packageService.addMember_Package(member_package), HttpStatus.OK);
    }

    @PostMapping("/add")
    public String addMember_Package(HttpServletRequest request, @RequestBody Member_Package member_package) {
        String jwt = jwtTokenFilter.getJwt(request);
        String username = jwtProvider.getUsernameFromToken(jwt);
        User user = userService.findByUsername(username).orElseThrow();
        Member member = memberService.getMemberByUser(user);
        member_package.setMember(member);
        Package pack = member_package.getPack();
        Long packId = pack.getId();
        Package pack1 = packageService.getPackage(packId);
        int totalPrice = pack1.getPrice() * member_package.getQuantity();
        httpSession.setAttribute("member_package", member_package);
        String packName = pack1.getName();
//        return new ResponseEntity<>(member_packageService.addMember_Package(member_package), HttpStatus.OK);
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(totalPrice, "Thanh toán gói "+packName, baseUrl);
        return vnpayUrl;
    }

    @PutMapping("admin/update/{id}")
    public ResponseEntity<Member_Package> updateMember_Package(@RequestBody Member_Package member_package, @PathVariable Long id){
        return new ResponseEntity<>(member_packageService.updateMember_Package(member_package, id), HttpStatus.OK);
    }

    @DeleteMapping("admin/delete/{id}")
    public ResponseEntity<?> deleteMember_Package(@PathVariable("id") Long id) {
        member_packageService.deleteMember_Package(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member_Package> getMember_Package(@PathVariable("id") Long id) {
        return new ResponseEntity<>(member_packageService.getMember_Package(id), HttpStatus.OK);
    }
}
