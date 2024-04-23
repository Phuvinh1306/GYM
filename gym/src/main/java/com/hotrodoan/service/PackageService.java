package com.hotrodoan.service;

import com.hotrodoan.model.Package;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PackageService {
    Package addPackage(Package pack);
    List<Package> getAllPackage();
    Package getPackage(Long id);
    void deletePackage(Long id);
    Package updatePackage(Package pack, Long id);

    Page<Package> getAllPackage(Pageable pageable);
    Page<Package> findPackagesByNameContaining(String name, Pageable pageable);
}
