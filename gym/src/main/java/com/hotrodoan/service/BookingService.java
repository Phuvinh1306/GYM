package com.hotrodoan.service;

import com.hotrodoan.model.Booking;
import com.hotrodoan.model.Employee;
import com.hotrodoan.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BookingService {
    Booking addBooking(Booking booking);
    void deleteBooking(Long id);
    Page<Booking> getAllBooking(Pageable pageable);

    Booking updateBooking(Booking booking, Long id);

    boolean existsBookingForEmployeeInTimeRange(Employee employeeId, Date bookingTime, Date endTime);

    Optional<Booking> findById(Long id);

    List<Booking> findByEndTimeBefore(Timestamp currentTime);

    Page<Booking> findBookingByMemberId(Long memberId, Pageable pageable);
    List<Employee> findEmployeesWithNoBookingInTimeRange(Date bookingTime, Date endTime);

//    List<Employee> findEmployeeByBookingTimeBetween(Date start, Date end);
//    Booking getBookingById(Long id);
//    List<Booking> getBookingByMember(Member member);
//    List<Booking> getBookingByEmployeeId(Long employeeId);
//    List<Booking> getBookingByBookingTime(Date bookingTime);
//    List<Booking> getBookingByBookingTimeBetween(Date start, Date end);
//    List<Booking> getBookingByBookingTimeBefore(Date bookingTime);
//    List<Booking> getBookingByBookingTimeAfter(Date bookingTime);
//    List<Booking> getBookingByBookingTimeBeforeAndBookingTimeAfter(Date before, Date after);
//    List<Booking> getBookingByBookingTimeBeforeAndBookingTimeAfterAndMemberId(Date before, Date after, Long memberId);
//    List<Booking> getBookingByBookingTimeBeforeAndBookingTimeAfterAndEmployeeId(Date before, Date after, Long employeeId);
//    List<Booking> getBookingByBookingTimeBeforeAndBookingTimeAfterAndMemberIdAndEmployeeId(Date before, Date after, Long memberId, Long employeeId);
//    List<Booking> getBookingByBookingTimeBeforeAndBookingTimeAfterAndMemberIdAndEmployeeIdAndBookingTime(Date before, Date after, Long memberId, Long employeeId, Date bookingTime);
//    List<Booking> getBookingByBookingTimeBeforeAndBookingTimeAfterAndMemberIdAndEmployeeIdAndBookingTimeBetween(Date before, Date after, Long memberId, Long employeeId, Date start, Date end);
//    List<Booking> getBookingByBookingTimeBeforeAndBookingTimeAfterAndMemberIdAndEmployeeIdAndBookingTimeBefore(Date before, Date after, Long memberId, Long employeeId, Date bookingTime);
//    List<Booking> getBookingByBookingTimeBeforeAndBookingTimeAfterAndMemberIdAndEmployeeIdAndBookingTimeAfter(Date before, Date after, Long memberId, Long employeeId, Date bookingTime);
//    List<Booking> getBookingByBookingTimeBeforeAndBookingTimeAfterAndMemberIdAndEmployeeIdAndBookingTimeBeforeAndBookingTimeAfter(Date before, Date after, Long memberId, Long employeeId, Date bookingTime);
//    List<Booking> getBookingByBookingTimeBeforeAndBookingTimeAfterAndMemberIdAndEmployeeIdAndBookingTimeBeforeAndBookingTimeAfterAndBookingTime(Date before, Date after, Long memberId, Long employeeId, Date bookingTime);
}
