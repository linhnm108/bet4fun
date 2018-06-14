package com.fafc.bet4fun.services.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fafc.bet4fun.entities.SyncHistory;
import com.fafc.bet4fun.repositories.SyncHistoryRepository;
import com.fafc.bet4fun.services.SyncHistoryService;

@Service
public class SyncHistoryServiceImpl implements SyncHistoryService {

    @Autowired
    SyncHistoryRepository syncHistoryRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public SyncHistory saveSyncHistory(SyncHistory sync) {
        return this.syncHistoryRepository.save(sync);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<SyncHistory> getAllSyncHistory() {
            Query query = entityManager.createNativeQuery("SELECT * FROM sync_history ORDER BY to_date DESC", SyncHistory.class);
            return query.getResultList();
    }

}
