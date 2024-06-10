package com.hotrodoan.controller;

import com.hotrodoan.dto.response.VNPayResponse;
import com.hotrodoan.model.*;
import com.hotrodoan.model.Package;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@SessionAttributes("m_p")
@RequestMapping("/member_packages")
@CrossOrigin(origins = "*")
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
    private Member_PackageSubService member_packageSubService;

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
    @Transactional
    public ResponseEntity<VNPayResponse> addMember_Package(HttpServletRequest request, @RequestBody Member_PackageSub memberPackageSub) {
        String jwt = jwtTokenFilter.getJwt(request);
        String username = jwtProvider.getUsernameFromToken(jwt);
        User user = userService.findByUsername(username).orElseThrow();
        Member member = memberService.getMemberByUser(user);

        if (member_packageService.checkExistsByMember(member)) {
            return new ResponseEntity<>(new VNPayResponse("Member already has a package", ""), HttpStatus.BAD_REQUEST);
        }
        else {
            memberPackageSub.setMember(member);
            Member_PackageSub newMemberPackageSub = member_packageSubService.createMember_PackageSub(memberPackageSub);
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String vnpayUrl = vnPayService.createOrder(newMemberPackageSub.getAmount(), "pack"+newMemberPackageSub.getId(), baseUrl);
            return new ResponseEntity<>(new VNPayResponse("pay for "+newMemberPackageSub.getPack().getName(), vnpayUrl), HttpStatus.OK);
        }
    }

    @PutMapping("admin/update/{id}")
    public ResponseEntity<Member_Package> updateMember_Package(@RequestBody Member_Package member_package, @PathVariable Long id){
        return new ResponseEntity<>(member_packageService.updateMember_Package(member_package, id), HttpStatus.OK);
    }

    @DeleteMapping("admin/delete/{id}")
    @Transactional
    public ResponseEntity<?> deleteMember_Package(@PathVariable("id") Long id) {
        member_packageService.deleteMember_Package(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member_Package> getMember_Package(@PathVariable("id") Long id) {
        return new ResponseEntity<>(member_packageService.getMember_Package(id), HttpStatus.OK);
    }
}
