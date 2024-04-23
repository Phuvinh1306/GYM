package com.hotrodoan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hotrodoan.model.History;
import com.hotrodoan.model.Member;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long>{
    List<History> findByMember(Member member);
}
