package com.hotrodoan.controller;

import com.hotrodoan.dto.request.ChangePasswordForm;
import com.hotrodoan.dto.request.UpdateProfileForm;
import com.hotrodoan.dto.response.ResponseMessage;
import com.hotrodoan.dto.response.ResponseProfile;
import com.hotrodoan.model.*;
import com.hotrodoan.model.Package;
import com.hotrodoan.security.jwt.JwtProvider;
import com.hotrodoan.security.jwt.JwtTokenFilter;
import com.hotrodoan.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/profile")
@CrossOrigin(origins = "*")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ImageService imageService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private Member_PackageService member_packageService;

    @GetMapping("")
    public ResponseEntity<ResponseProfile> getProfile(HttpServletRequest request) {
        String jwt = jwtTokenFilter.getJwt(request);
        String username = jwtProvider.getUsernameFromToken(jwt);
        User user = userService.findByUsername(username).orElseThrow();
        Member member = memberService.getMemberByUser(user);

        ResponseProfile responseProfile = new ResponseProfile();
        responseProfile.setName(user.getName());
        responseProfile.setUsername(user.getUsername());
        responseProfile.setPhone(member.getPhone());
        responseProfile.setEmail(user.getEmail());
        responseProfile.setCccd(member.getCccd());
        responseProfile.setSex(member.getSex());
        responseProfile.setCreatedAt(member.getCreatedAt());
        responseProfile.setGymBranch(member.getGymBranch());
        List<Member_Package> member_packages = member_packageService.getMember_PackageByMember(member);
        List<Package> packs = new ArrayList<>();
        for (Member_Package member_package : member_packages) {
            packs.add(member_package.getPack());
        }
        responseProfile.setPacks(packs);
        return new ResponseEntity<>(responseProfile, HttpStatus.OK);
    }

    @GetMapping("/avatar")
    public ResponseEntity<Resource> viewImage(HttpServletRequest request) throws Exception {
        String token = jwtTokenFilter.getJwt(request);
        String username = jwtProvider.getUsernameFromToken(token);
        User user = userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        Image image = user.getImage();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFileType()))
                .body(new ByteArrayResource(image.getData()));
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Member> updateProfile(HttpServletRequest request,
                                           @RequestParam(value = "name", required = false) String name,
                                           @RequestParam(value = "phone", required = false) String phone,
                                           @RequestParam(value = "cccd", required = false) String cccd,
                                           @RequestParam(value = "address", required = false) String address,
                                           @RequestParam(value = "sex", required = false) String sex,
                                           @RequestParam(value = "gymBranch", required = false) GymBranch gymBranch,
                                           @RequestParam(value = "file", required = false) MultipartFile file) throws Exception {
        String jwt = jwtTokenFilter.getJwt(request);
        String username = jwtProvider.getUsernameFromToken(jwt);
        User user = userService.findByUsername(username).orElseThrow();
        Long id = user.getId();
        Member member = profileService.getProfileMember(user);
        user.setName(name);
        member.setPhone(phone);
        member.setAddress(address);
        member.setCccd(cccd);
        member.setSex(sex);
        member.setGymBranch(gymBranch);

        if (file != null && !file.isEmpty()){
            String oldImageId = null;
            if (user.getImage() != null && !user.getImage().equals("")) {
                oldImageId = user.getImage().getId();
            }
            Image image = imageService.saveImage(file);
            user.setImage(image);

            userService.update(user, id);
            if (oldImageId != null) {
                imageService.deleteImage(oldImageId);
            }
        }

        profileService.updateProfile(member, member.getId());
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(HttpServletRequest request, @RequestBody ChangePasswordForm changePasswordForm) {
        String jwt = jwtTokenFilter.getJwt(request);
        String username = jwtProvider.getUsernameFromToken(jwt);
        User user = userService.findByUsername(username).orElseThrow();

        if(passwordEncoder.matches(changePasswordForm.getOldPassword(), user.getPassword())){
            if(!changePasswordForm.getNewPassword().equals(changePasswordForm.getConfirmPassword())){
                return new ResponseEntity<>(new ResponseMessage("confirm_password_not_match"), HttpStatus.OK);
            }
            user.setPassword(passwordEncoder.encode(changePasswordForm.getNewPassword()));
            userService.save(user);
            return new ResponseEntity<>(new ResponseMessage("change_password_success"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseMessage("change_password_fail"), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProfile(HttpServletRequest request) {
        String jwt = jwtTokenFilter.getJwt(request);
        String username = jwtProvider.getUsernameFromToken(jwt);
        User user = userService.findByUsername(username).orElseThrow();
        profileService.deleteProfile(user.getId());
        return new ResponseEntity<>(new ResponseMessage("deleted"), HttpStatus.OK);
    }
}
