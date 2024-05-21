package com.hotrodoan.repository;

import com.hotrodoan.model.GymBranch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GymBranchRepository extends JpaRepository<GymBranch, Long> {
    Page<GymBranch> findByNameOrAddressContaining(String name, String address, Pageable pageable);
}
