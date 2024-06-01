package com.hotrodoan.controller;

import com.hotrodoan.service.VnPayPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;

@RestController
@RequestMapping("/reports")
@CrossOrigin(origins = "*")
public class ReportController {
    @Autowired
    private VnPayPaymentService vnPayPaymentService;

    @RequestMapping("/revenue")
    public ResponseEntity<?> getTotalRevenueByMonth(@RequestParam(defaultValue = "2024-01-01") Date startTime,
                                                    @RequestParam(defaultValue = "2024-12-31") Date endTime) {
        return new ResponseEntity<>(vnPayPaymentService.getTotalRevenueByMonth(startTime, endTime), HttpStatus.OK);
    }
}
