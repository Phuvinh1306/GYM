package com.hotrodoan.service;

import com.hotrodoan.dto.request.BookingSub;

public interface BookingSubService {
    BookingSub createBookingSub(BookingSub bookingSub);
    BookingSub updateBookingSub(BookingSub bookingSub, Long id);
    BookingSub getBookingSub(Long id);
    void deleteBookingSub(Long id);
}
