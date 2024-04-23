package com.hotrodoan.service;

import com.hotrodoan.model.Member;
import com.hotrodoan.model.Member_Package;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface Member_PackageService {
    Page<Member_Package> getAllMember_Package(Pageable pageable);
    Page<Member_Package> findMember_PackageByMember(Member member, Pageable pageable);

    Member_Package addMember_Package(Member_Package member_package);
    Member_Package updateMember_Package(Member_Package member_package, Long id);
    void deleteMember_Package(Long id);
    Member_Package getMember_Package(Long id);
}
