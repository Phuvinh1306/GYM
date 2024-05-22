package com.hotrodoan.service;

import com.hotrodoan.model.Member_PackageSub;

public interface Member_PackageSubService {
    Member_PackageSub createMember_PackageSub(Member_PackageSub member_packageSub);
    Member_PackageSub updateMember_PackageSub(Member_PackageSub member_packageSub, Long id);
    Member_PackageSub getMember_PackageSub(Long id);
    void deleteMember_PackageSub(Long id);
}
