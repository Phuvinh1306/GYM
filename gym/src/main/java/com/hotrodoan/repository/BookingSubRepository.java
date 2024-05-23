package com.hotrodoan.repository;

import com.hotrodoan.dto.request.BookingSub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingSubRepository extends JpaRepository<BookingSub, Long> {
}
