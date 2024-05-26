package com.hotrodoan.controller;

import com.hotrodoan.config.Utility;
import com.hotrodoan.dto.request.LoginForm;
import com.hotrodoan.dto.request.SignupForm;
import com.hotrodoan.dto.response.JwtResponse;
import com.hotrodoan.dto.response.ResponseMessage;
import com.hotrodoan.model.Member;
import com.hotrodoan.model.Role;
import com.hotrodoan.model.RoleName;
import com.hotrodoan.model.User;
import com.hotrodoan.security.jwt.JwtProvider;
import com.hotrodoan.security.jwt.JwtTokenFilter;
import com.hotrodoan.security.userdetail.UserDetail;
import com.hotrodoan.service.MemberService;
import com.hotrodoan.service.RoleService;
import com.hotrodoan.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import net.bytebuddy.utility.RandomString;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<?> register(@Valid @RequestBody SignupForm signupForm, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        if(userService.existsByUsername(signupForm.getUsername())){
            return new ResponseEntity<>(new ResponseMessage("username_exists"), HttpStatus.OK);
        }
        if(userService.existsByEmail(signupForm.getEmail())){
            return new ResponseEntity<>(new ResponseMessage("email_exists"), HttpStatus.OK);
        }

        User user = new User(signupForm.getName(), signupForm.getUsername(), passwordEncoder.encode(signupForm.getPassword()), signupForm.getEmail(), signupForm.getAvatar());
        Set<String> strRoles = signupForm.getRoles();
        Set<Role> roles = new HashSet<>();
        if (strRoles == null){
            Role userRole = roleService.findByName(RoleName.USER).orElseThrow(() -> new RuntimeException("Role not found"));
            roles.add(userRole);
        }
        else {
            strRoles.forEach(role -> {
                switch (role){
                    case "admin":
                        Role adminRole = roleService.findByName(RoleName.ADMIN).orElseThrow(() -> new RuntimeException("Role not found"));
                        roles.add(adminRole);
                        break;
                    case "employee":
                        Role pmRole = roleService.findByName(RoleName.EMPLOYEE).orElseThrow(() -> new RuntimeException("Role not found"));
                        roles.add(pmRole);
                        break;
                    case "manager":
                        Role managerRole = roleService.findByName(RoleName.MANAGER).orElseThrow(() -> new RuntimeException("Role not found"));
                        roles.add(managerRole);
                        break;
                    default:
                        Role userRole = roleService.findByName(RoleName.USER).orElseThrow(() -> new RuntimeException("Role not found"));
                        roles.add(userRole);
                        break;
                }
            });
        }
//        roles.add(roleService.findByName(RoleName.USER).orElseThrow(() -> new RuntimeException("Role not found")));
        user.setRoles(roles);
        user.setEnabled(false);

        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);

        userService.save(user);

        String siteURL = Utility.getSiteURL(request);
        sendVertificationEmail(user, siteURL);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Member member = new Member();
        member.setUser(user);
        member.setCreatedAt(timestamp);
        memberService.addMember(member);
        return new ResponseEntity<>(new ResponseMessage("create_success"), HttpStatus.OK);
    }

    private void sendVertificationEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String subject = "Please verify your registration";
        String senderName = "Hotrodoan";

        String mailContent = "Dear " + user.getName() + ",<br>";
        mailContent += "Please click the link below to verify your registration:<br>";
        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();
        mailContent += "<h3><a href='" + verifyURL + "'>Verify</a></h3>";
        mailContent += "Thank you!";
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("admingrgym", senderName);
        helper.setTo(user.getEmail());
        helper.setSubject(subject);
        helper.setText(mailContent, true);
        javaMailSender.send(message);
//                + "Please click the link below to verify your registration:<br>"
//                + "<h3><a href='http://localhost:8080/verify?code=" + user.getVerificationCode() + "'>Verify</a></h3>"
//                + "Thank you!";
//        userService.sendEmail(user.getEmail(), subject, mailContent);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam("code") String code){
        boolean verified = userService.verify(code);
//        String pageTitle = verified ? "Verification Success" : "Verification Failed";
        String message = verified ? "Your account has been verified. You can now login." : "Verification failed. Please contact the administrator.";
        return new ResponseEntity<>(new ResponseMessage(message), HttpStatus.OK);
//        return message;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginForm loginForm){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword())
        );

        // Lấy thông tin người dùng từ Principal
        UserDetail userDetail = (UserDetail) authentication.getPrincipal();
        System.out.println(userDetail.getAuthorities());

        // Kiểm tra trạng thái enabled của người dùng
        if (!userDetail.isEnabled()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("User is not enabled. Please contact administrator.");
        }else {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtProvider.createToken(authentication);
            return ResponseEntity.ok(new JwtResponse(token, userDetail.getId(), userDetail.getName(), userDetail.getAuthorities(), userDetail.getAvatar()));
        }
    }

    

//    @GetMapping("/token")
//    public Map<String, String> getToken() throws FirebaseAuthException {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String authorities = authentication.getAuthorities()
//                .stream().map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(","));
//        String customToken = firebaseAuth.createCustomToken("thovuavua", Collections.singletonMap("authorities", authorities));
//        return Collections.singletonMap("token", customToken);
//    }

//    @PutMapping("/change-avatar")
//    public ResponseEntity<?> changeAvatar(HttpServletRequest request, @Valid @RequestBody ChangeAvatar changeAvatar){
//        String jwt = jwtTokenFilter.getJwt(request);
//        String username = jwtProvider.getUsernameFromToken(jwt);
//        User user;
//        try {
//            if (changeAvatar.getAvatar() == null || changeAvatar.getAvatar().trim().equals("")){
//                return new ResponseEntity<>(new ResponseMessage("no"), HttpStatus.OK);
//            }else {
//                user = userService.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"+ username));
//                user.setAvatar(changeAvatar.getAvatar());
//                userService.save(user);
//            }
//            return new ResponseEntity<>(new ResponseMessage("yes"), HttpStatus.OK);
//        }catch (UsernameNotFoundException exception){
//            return new ResponseEntity<>(new ResponseMessage(exception.getMessage()), HttpStatus.NOT_FOUND);
//        }
//    }
}
