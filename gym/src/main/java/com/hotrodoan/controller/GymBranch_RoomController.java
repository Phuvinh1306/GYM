package com.hotrodoan.controller;

import com.hotrodoan.service.GymBranch_RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gym-branch-rooms")
@CrossOrigin(origins = "*")
public class GymBranch_RoomController {
    @Autowired
    private GymBranch_RoomService gymBranchRoomService;
}
