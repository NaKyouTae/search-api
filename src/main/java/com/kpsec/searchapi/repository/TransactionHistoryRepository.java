package com.kpsec.searchapi.repository;

import com.kpsec.searchapi.model.entity.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Create by na kyutae 2021-10-29.
 */
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, String> {
}
