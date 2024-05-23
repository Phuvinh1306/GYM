package com.hotrodoan.service.impl;

import com.hotrodoan.dto.request.BookingSub;
import com.hotrodoan.repository.BookingSubRepository;
import com.hotrodoan.service.BookingSubService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingSubServiceImpl implements BookingSubService {
    @Autowired
    private BookingSubRepository bookingSubRepository;

    @Override
    public BookingSub createBookingSub(BookingSub bookingSub) {
        return bookingSubRepository.save(bookingSub);
    }

    @Override
    public BookingSub updateBookingSub(BookingSub bookingSub, Long id) {
        return null;
    }

    @Override
    public BookingSub getBookingSub(Long id) {
        return bookingSubRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
    }

    @Override
    public void deleteBookingSub(Long id) {
        bookingSubRepository.deleteById(id);
    }
}
