package com.hotrodoan.schedule;

import com.hotrodoan.model.Member_Package;
import com.hotrodoan.service.Member_PackageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.List;

@Component
public class Member_PackageScheduler {
    private static final Logger logger = LoggerFactory.getLogger(Member_PackageScheduler.class);
    @Autowired
    private Member_PackageService member_packageService;

    @Scheduled(fixedRate = 60000)
    public void checkAndDeleteMember_Package(){
        Date today = new Date(System.currentTimeMillis());
        List<Member_Package> expiredMember_Packages = member_packageService.getTheExpriredMember_Package(today);
        for (Member_Package expiredMember_Package : expiredMember_Packages){
            member_packageService.deleteMember_Package(expiredMember_Package.getId());
        }
    }
}
