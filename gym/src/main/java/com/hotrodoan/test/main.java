package com.hotrodoan.test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

public class main {
    public static void main(String[] args) {
        CurrentTime currentTime = new CurrentTime();
        Timestamp timestamp = new Timestamp(currentTime.getCurrentTime().getTime());
        System.out.println(timestamp);
    }
}
