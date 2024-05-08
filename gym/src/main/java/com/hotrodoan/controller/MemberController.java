package com.hotrodoan.controller;

import com.hotrodoan.dto.response.ResponseMessage;
import com.hotrodoan.model.Employee;
import com.hotrodoan.model.Member;
import com.hotrodoan.model.User;
import com.hotrodoan.security.jwt.JwtProvider;
import com.hotrodoan.security.jwt.JwtTokenFilter;
import com.hotrodoan.service.MemberService;
import com.hotrodoan.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
@CrossOrigin(origins = "*")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    private UserService userService;

//    @GetMapping("")
//    public ResponseEntity<Page<Member>> getAllMember(@RequestParam(defaultValue = "0") int page,
//                                                     @RequestParam(defaultValue = "10") int size,
//                                                     @RequestParam(defaultValue = "id") String sortBy,
//                                                     @RequestParam(defaultValue = "desc") String order) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sortBy)));
//        return new ResponseEntity<>(memberService.getAllMember(pageable), HttpStatus.OK);
//    }

    @PostMapping("/admin/add")
    public ResponseEntity<Member> addMember(@RequestBody Member member) {
        return new ResponseEntity<>(memberService.addMember(member), HttpStatus.OK);
    }

//    @PostMapping("/add")
//    public ResponseEntity<Member> addMember(HttpServletRequest request, @RequestBody Member member) {
//        String jwt = jwtTokenFilter.getJwt(request);
//        String username = jwtProvider.getUsernameFromToken(jwt);
//        User user = userService.findByUsername(username).orElseThrow();
//        member.setUser(user);
//        return new ResponseEntity<>(memberService.addMember(member), HttpStatus.OK);
//    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Member> updateMember(@RequestBody Member member, @PathVariable Long id){
        return new ResponseEntity<>(memberService.updateMember(member, id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable("id") Long id) {
        memberService.deleteMember(id);
        return new ResponseEntity<>(new ResponseMessage("deleted"), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMember(@PathVariable("id") Long id) {
        return new ResponseEntity<>(memberService.getMember(id), HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<Page<Member>> findMembersByNameContaining(@RequestParam(defaultValue = "") String name,
                                                                        @RequestParam(defaultValue = "0") int page,
                                                                        @RequestParam(defaultValue = "10") int size,
                                                                        @RequestParam(defaultValue = "id") String sortBy,
                                                                        @RequestParam(defaultValue = "desc") String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sortBy)));
        return new ResponseEntity<>(memberService.findMembersByNameContaining(name, pageable), HttpStatus.OK);
    }
}
