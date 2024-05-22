package com.hotrodoan.repository;

import com.hotrodoan.model.Member_PackageSub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Member_PackageSubRepository extends JpaRepository<Member_PackageSub, Long> {
}
