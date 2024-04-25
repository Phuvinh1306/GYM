package com.hotrodoan.repository;

import com.hotrodoan.model.VnPayPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VnPayPaymentRepository extends JpaRepository<VnPayPayment, Long> {
}
