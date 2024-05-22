package com.hotrodoan.repository;

import com.hotrodoan.model.GymBranch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GymBranchRepository extends JpaRepository<GymBranch, Long> {
    @Query("SELECT gb FROM GymBranch gb WHERE CONCAT(gb.name, ' ', gb.address) LIKE %:keyword%")
    Page<GymBranch> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
