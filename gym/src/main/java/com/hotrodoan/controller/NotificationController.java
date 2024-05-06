package com.hotrodoan.controller;

import com.hotrodoan.model.NotificationMessage;
import com.hotrodoan.service.FirebaseMessagingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
    // @Autowired
    // FirebaseMessagingService firebaseMessagingService;

    // @PostMapping
    // public String sendNotificationByToken(@RequestBody NotificationMessage notificationMessage){
    //     return firebaseMessagingService.sendNotificationByToken(notificationMessage);
    // }
}
