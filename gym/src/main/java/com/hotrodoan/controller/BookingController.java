package com.hotrodoan.controller;

import com.hotrodoan.dto.request.BookingSub;
import com.hotrodoan.dto.response.ResponseMessage;
import com.hotrodoan.dto.response.VNPayResponse;
import com.hotrodoan.exception.EmployeeNotfoundException;
import com.hotrodoan.model.Booking;
import com.hotrodoan.model.Member;
import com.hotrodoan.model.User;
import com.hotrodoan.model.WorkoutSession;
import com.hotrodoan.security.jwt.JwtProvider;
import com.hotrodoan.security.jwt.JwtTokenFilter;
import com.hotrodoan.service.*;
import com.hotrodoan.test.CurrentTime;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "*")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    @Autowired
    private UserService userService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private WorkoutSessionService workoutSessionService;

    @Autowired
    private BookingSubService bookingSubService;

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private Member_PackageService member_packageService;

    @GetMapping("/admin/all")
    public ResponseEntity<?> pageBooking(@PageableDefault(sort = "id", direction = Sort.Direction.DESC)Pageable pageable) {
        Page<Booking> bookings = bookingService.getAllBooking(pageable);
        if (bookings.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<?> pageBooking(HttpServletRequest request, @PageableDefault(sort = "id", direction = Sort.Direction.DESC)Pageable pageable) {
        String jwt = jwtTokenFilter.getJwt(request);
        String username = jwtProvider.getUsernameFromToken(jwt);
        User user = userService.findByUsername(username).orElseThrow();
        Member member = memberService.getMemberByUser(user);
        Page<Booking> bookings = bookingService.findBookingByMemberId(member.getId(), pageable);
        if (bookings.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @PostMapping("/add")
    @Transactional
    public ResponseEntity<VNPayResponse> addBooking(HttpServletRequest request, @RequestBody BookingSub bookingSub) {
        if (employeeService.existsByIdAndPositionId(bookingSub.getEmployee().getId(), 2L)){
            String jwt = jwtTokenFilter.getJwt(request);
            String username = jwtProvider.getUsernameFromToken(jwt);
            User user = userService.findByUsername(username).orElseThrow();
            Member member = memberService.getMemberByUser(user);
            bookingSub.setMember(member);
            if (member_packageService.checkExistsByMember(member)) {
                bookingSub.setCost(0);
            }
            else {
                LocalDate bookingTime = bookingSub.getBookingTime().toLocalDateTime().toLocalDate();
                LocalDate endTime = bookingSub.getEndTime().toLocalDateTime().toLocalDate();
                int days = (int) ChronoUnit.DAYS.between(bookingTime, endTime);
                bookingSub.setCost(80000 * (days+1));
            }

            BookingSub newBookingSub = bookingSubService.createBookingSub(bookingSub);
//            WorkoutSession workoutSession = new WorkoutSession();
//            workoutSession.setBooking(newBooking);
//            workoutSessionService.addWorkoutSession(workoutSession);
            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            String vnpayUrl = vnPayService.createOrder(newBookingSub.getCost(), "booking"+newBookingSub.getId(), baseUrl);
            return new ResponseEntity<>(new VNPayResponse("pay for booking at "+newBookingSub.getBookingTime().toString(), vnpayUrl), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/add")
    public ResponseEntity<?> allEmployeePTCanBooking(@RequestParam("bookingTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date bookingTime,
                                                     @RequestParam("endTime") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date endTime) {
        return new ResponseEntity<>(bookingService.findEmployeesWithNoBookingInTimeRange(bookingTime, endTime), HttpStatus.OK);
    }

    @PostMapping("/admin/add")
    @Transactional
    public ResponseEntity<?> addBooking(@RequestBody Booking booking) {
        if (employeeService.existsByIdAndPositionId(booking.getEmployee().getId(), 2L)){
            return new ResponseEntity<>(bookingService.addBooking(booking), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBooking(id);
        return new ResponseEntity<>(new ResponseMessage("deleted"), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    @Transactional
    public ResponseEntity<?> updateBooking(HttpServletRequest request, @PathVariable Long id, @RequestBody Booking booking) {
        String jwt = jwtTokenFilter.getJwt(request);
        String username = jwtProvider.getUsernameFromToken(jwt);
        User user = userService.findByUsername(username).orElseThrow();
        boolean isEmployeeBooked = bookingService.existsBookingForEmployeeInTimeRange(booking.getEmployee(), booking.getBookingTime(), booking.getEndTime());
        Booking findBooking = bookingService.findById(id).orElseThrow(() -> new EmployeeNotfoundException("Không tìm thấy booking"));
        findBooking.setEmployee(booking.getEmployee());
        findBooking.setBookingTime(booking.getBookingTime());
        findBooking.setEndTime(booking.getEndTime());
        if (isEmployeeBooked){
            Member member = memberService.getMemberByUser(user);
            if (findBooking.getMember().equals(member)){
                return new ResponseEntity<>(bookingService.updateBooking(findBooking, id), HttpStatus.OK);
            }
            else {
                throw new EmployeeNotfoundException("Nhân viên đã có lịch đặt trong khoảng thời gian này");
            }
        }
        else {
            return new ResponseEntity<>(bookingService.updateBooking(findBooking, id), HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Long id) {
        return new ResponseEntity<>(bookingService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/show")
    public ResponseEntity<?> bookingsss() {
        CurrentTime currentTime = new CurrentTime();
        Timestamp timestamp = new Timestamp(currentTime.getCurrentTime().getTime());
        List<Booking> bookings = bookingService.findByEndTimeBefore(timestamp);
        if (bookings.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }
}
