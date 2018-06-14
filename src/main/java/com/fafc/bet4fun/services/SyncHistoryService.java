package com.fafc.bet4fun.services;

import java.util.List;

import com.fafc.bet4fun.entities.SyncHistory;

public interface SyncHistoryService {
    public SyncHistory saveSyncHistory(SyncHistory sync);
    public List<SyncHistory> getAllSyncHistory();
}
