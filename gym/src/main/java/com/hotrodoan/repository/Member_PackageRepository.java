package com.hotrodoan.repository;

import com.hotrodoan.model.Member;
import com.hotrodoan.model.Member_Package;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Member_PackageRepository extends JpaRepository<Member_Package, Long> {
    Page<Member_Package> findAll(Pageable pageable);

    Page<Member_Package> findByMember(Member member, Pageable pageable);
}
