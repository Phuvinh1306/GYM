package com.hotrodoan.repository;

import com.hotrodoan.model.Booking;

import com.hotrodoan.model.Employee;
import com.hotrodoan.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findById(Long id);

    Page<Booking> findByMemberId(Long memberId, Pageable pageable);

    @Query("SELECT COUNT(b) > 0 FROM Booking b " +
            "WHERE b.employee = :employee " +
            "AND ((b.bookingTime <= :endTime AND b.endTime >= :bookingTime) " +
            "OR (b.bookingTime >= :bookingTime AND b.bookingTime <= :endTime))")
    boolean existsBookingForEmployeeInTimeRange(@Param("employee") Employee employee,
                                                 @Param("bookingTime") Date bookingTime,
                                                 @Param("endTime") Date endTime);

    @Query("SELECT e FROM Employee e " +
            "WHERE e.position.id = 2 " +
            "AND NOT EXISTS (" +
            "    SELECT b FROM Booking b " +
            "    WHERE b.employee = e " +
            "    AND ((b.bookingTime <= :endTime AND b.endTime >= :bookingTime) " +
            "    OR (b.bookingTime >= :bookingTime AND b.bookingTime <= :endTime))" +
            ")")
    List<Employee> findEmployeesWithNoBookingInTimeRange(@Param("bookingTime") Date bookingTime,
                                                         @Param("endTime") Date endTime);


    List<Booking> findByEndTimeBefore(Timestamp currentTime);
}
