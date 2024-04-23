package com.hotrodoan.service;

import java.util.List;

import com.hotrodoan.model.History;
import com.hotrodoan.model.Member;

public interface HistoryService {
    History addHistory(History history);
    List<History> getAllHistories(Member member);
    History getHistory(Long id);
    void deleteHistory(Long id);
}
