package com.hotrodoan.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hotrodoan.model.Package;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageRepository extends JpaRepository<Package, Long>{
    Page<Package> findAll(Pageable pageable);
    Page<Package> findByNameContaining(String name, Pageable pageable);
}
