package com.hotrodoan.controller;

import com.hotrodoan.dto.response.ResponseMessage;
import com.hotrodoan.model.MemberCard;
import com.hotrodoan.service.MemberCardService;
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
@RequestMapping("/member-cards")
@CrossOrigin(origins = "*")
public class MemberCardController {
    @Autowired
    private MemberCardService memberCardService;

    @GetMapping("")
    public ResponseEntity<Page<MemberCard>> getAllMemberCard(@RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam(defaultValue = "id") String sortBy,
                                                             @RequestParam(defaultValue = "desc") String order) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sortBy)));
        return new ResponseEntity<>(memberCardService.getAllMemberCard(pageable), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<MemberCard> addMemberCard(@RequestBody MemberCard memberCard) {
        return new ResponseEntity<>(memberCardService.addMemberCard(memberCard), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<MemberCard> updateMemberCard(@RequestBody MemberCard memberCard, @PathVariable Long id) {
        return new ResponseEntity<>(memberCardService.updateMemberCard(memberCard, id), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMemberCard(@PathVariable("id") Long id) {
        memberCardService.deleteMemberCard(id);
        return new ResponseEntity<>(new ResponseMessage("deleted"), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberCard> getMemberCard(@PathVariable("id") Long id) {
        return new ResponseEntity<>(memberCardService.getMemberCard(id), HttpStatus.OK);
    }
}
