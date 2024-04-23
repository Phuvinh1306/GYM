package com.hotrodoan.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hotrodoan.model.History;
import com.hotrodoan.model.Member;
import com.hotrodoan.repository.HistoryRepository;
import com.hotrodoan.service.HistoryService;

@Service
public class HistoryServiceImpl implements HistoryService{
    @Autowired
    private HistoryRepository historyRepository;

    @Override
    public History addHistory(History history) {
        return historyRepository.save(history);
    }

    @Override
    public List<History> getAllHistories(Member member) {
        return historyRepository.findByMember(member);
    }

    @Override
    public History getHistory(Long id) {
        return historyRepository.findById(id).orElseThrow(() -> new RuntimeException("History not found"));
    }

    @Override
    public void deleteHistory(Long id) {
        if (!historyRepository.existsById(id)) {
            throw new RuntimeException("History not found");
        }
        historyRepository.deleteById(id);
    }
}
