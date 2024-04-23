package com.hotrodoan.service.impl;

import com.hotrodoan.exception.EmployeeNotfoundException;
import com.hotrodoan.model.Booking;
import com.hotrodoan.model.Employee;
import com.hotrodoan.model.Member;
import com.hotrodoan.model.User;
import com.hotrodoan.repository.BookingRepository;
import com.hotrodoan.security.userdetail.CustomUserDetailService;
import com.hotrodoan.service.BookingService;
import com.hotrodoan.service.MemberService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private MemberService memberService;

    @Override
    public Booking addBooking(Booking booking) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication.isAuthenticated()) {
//            String username = authentication.getName();
//            User user = userServiceImpl.findByUsername(username).orElseThrow(() -> new EmployeeNotfoundException("Không tìm thấy user"));
//            Member member = memberService.getMemberByUser(user);
//            booking.setMember(member);
//        }
        boolean isEmployeeBooked = bookingRepository.existsBookingForEmployeeInTimeRange(booking.getEmployee(), booking.getBookingTime(), booking.getEndTime());
        if (isEmployeeBooked) {
            throw new EmployeeNotfoundException("Nhân viên đã có lịch đặt trong khoảng thời gian này");
        }
        else {
            return bookingRepository.save(booking);
        }
    }

    @Override
    public void deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new EmployeeNotfoundException("Không tìm thấy booking");
        }
        bookingRepository.deleteById(id);
    }

    @Override
    public Page<Booking> getAllBooking(Pageable pageable) {
        return bookingRepository.findAll(pageable);
    }

    @Override
    public Booking updateBooking(Booking booking, Long id) {
//        boolean isEmployeeBooked = bookingRepository.existsBookingForEmployeeInTimeRange(booking.getEmployee(), booking.getBookingTime(), booking.getEndTime());
//        if (isEmployeeBooked && bookingRepository.findById(id).get().getEmployee().getId().equals(booking.getEmployee().getId())) {
//            return bookingRepository.findById(id).map(bk -> {
//                bk.setMember(booking.getMember());
//                bk.setEmployee(booking.getEmployee());
//                bk.setBookingTime(booking.getBookingTime());
//                bk.setEndTime(booking.getEndTime());
//                return bookingRepository.save(bk);
//            }).orElseThrow(() -> new EmployeeNotfoundException("Không tìm thấy booking"));
//        }
//        else if (!isEmployeeBooked) {
//            return bookingRepository.findById(id).map(bk -> {
//                bk.setMember(booking.getMember());
//                bk.setEmployee(booking.getEmployee());
//                bk.setBookingTime(booking.getBookingTime());
//                bk.setEndTime(booking.getEndTime());
//                return bookingRepository.save(bk);
//            }).orElseThrow(() -> new EmployeeNotfoundException("Không tìm thấy booking"));
//        }
//        else {
//            throw new EmployeeNotfoundException("Nhân viên đã có lịch đặt trong khoảng thời gian này");
//        }
        return bookingRepository.findById(id).map(bk -> {
            bk.setMember(booking.getMember());
            bk.setEmployee(booking.getEmployee());
            bk.setBookingTime(booking.getBookingTime());
            bk.setEndTime(booking.getEndTime());
            return bookingRepository.save(bk);
        }).orElseThrow(() -> new EmployeeNotfoundException("Không tìm thấy booking"));
    }

    @Override
    public boolean existsBookingForEmployeeInTimeRange(Employee employeeId, Date bookingTime, Date endTime) {
        return bookingRepository.existsBookingForEmployeeInTimeRange(employeeId, bookingTime, endTime);
    }

    @Override
    public Optional<Booking> findById(Long id) {
        return bookingRepository.findById(id);
    }

    @Override
    public List<Booking> findByEndTimeBefore(Timestamp currentTime) {
        return bookingRepository.findByEndTimeBefore(currentTime);
    }

    @Override
    public Page<Booking> findBookingByMemberId(Long memberId, Pageable pageable) {
        return bookingRepository.findByMemberId(memberId, pageable);
    }
}
