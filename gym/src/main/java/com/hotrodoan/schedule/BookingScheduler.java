package com.hotrodoan.schedule;

import com.hotrodoan.model.Booking;
import com.hotrodoan.model.History;
import com.hotrodoan.service.BookingService;
import com.hotrodoan.service.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

//@Component
public class BookingScheduler {
    private static final Logger logger = LoggerFactory.getLogger(BookingScheduler.class);
    @Autowired
    private BookingService bookingService;
    @Autowired
    private HistoryService historyService;

    @Scheduled(fixedRate = 3600000)
    public void addHistoryFromBooking() {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        List<Booking> expiredBookings = bookingService.findByEndTimeBefore(currentTime);
        for (Booking booking : expiredBookings) {
            History history = new History();
            history.setMember(booking.getMember());
            history.setEmployee(booking.getEmployee());
            history.setBookingTime(booking.getBookingTime());
            history.setEndTime(booking.getEndTime());
            historyService.addHistory(history);
            bookingService.deleteBooking(booking.getId());
        }
    }
}
