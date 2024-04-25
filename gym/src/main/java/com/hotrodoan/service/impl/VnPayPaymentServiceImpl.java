package com.hotrodoan.service.impl;

import com.hotrodoan.model.VnPayPayment;
import com.hotrodoan.repository.VnPayPaymentRepository;
import com.hotrodoan.service.VnPayPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VnPayPaymentServiceImpl implements VnPayPaymentService {
    @Autowired
    private VnPayPaymentRepository vnPayPaymentRepository;
    @Override
    public VnPayPayment addVnPayPayment(VnPayPayment vnPayPayment) {
        return vnPayPaymentRepository.save(vnPayPayment);
    }
}
