package com.hotrodoan.test;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CurrentTime {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private Date currentTime;

    public Date getCurrentTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        this.currentTime = Date.from(localDateTime.toInstant(ZoneOffset.UTC));
        return this.currentTime;
    }
}
