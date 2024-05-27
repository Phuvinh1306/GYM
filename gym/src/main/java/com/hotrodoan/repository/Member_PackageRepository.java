package com.hotrodoan.repository;

import com.hotrodoan.model.Member;
import com.hotrodoan.model.Member_Package;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface Member_PackageRepository extends JpaRepository<Member_Package, Long> {
    Page<Member_Package> findAll(Pageable pageable);
    Page<Member_Package> findByMember(Member member, Pageable pageable);
    boolean existsByMember(Member member);
    List<Member_Package> findByEndDateBefore(Date today);
    List<Member_Package> findByMember(Member member);
}
