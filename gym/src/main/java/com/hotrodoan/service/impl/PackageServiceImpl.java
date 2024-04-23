package com.hotrodoan.service.impl;

import com.hotrodoan.model.Package;
import com.hotrodoan.repository.PackageRepository;
import com.hotrodoan.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PackageServiceImpl implements PackageService {
    @Autowired PackageRepository packageRepository;

    @Override
    public Package addPackage(Package pack) {
        return packageRepository.save(pack);
    }

    @Override
    public List<Package> getAllPackage() {
        return packageRepository.findAll();
    }

    @Override
    public Package getPackage(Long id) {
        return packageRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy gói dịch vụ"));
    }

    @Override
    public void deletePackage(Long id) {
        if (!packageRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy gói dịch vụ");
        }
        packageRepository.deleteById(id);
    }

    @Override
    public Package updatePackage(Package pack, Long id) {
        return packageRepository.findById(id).map(p -> {
            p.setName(pack.getName());
            p.setPrice(pack.getPrice());
            p.setDuration(pack.getDuration());
            return packageRepository.save(p);
        }).orElseThrow(() -> new RuntimeException("Không tìm thấy gói dịch vụ"));
    }

    @Override
    public Page<Package> getAllPackage(Pageable pageable) {
        return packageRepository.findAll(pageable);
    }

    @Override
    public Page<Package> findPackagesByNameContaining(String name, Pageable pageable) {
        return packageRepository.findByNameContaining(name, pageable);
    }
}
