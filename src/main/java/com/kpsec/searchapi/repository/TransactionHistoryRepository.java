package com.kpsec.searchapi.repository;

import com.kpsec.searchapi.model.entity.TransactionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Create by na kyutae 2021-10-29.
 */
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistory, Long> {
    List<TransactionHistory> findByAccountNoAndTransactionDateStartingWithAndCancelYnFalse(String accountNo, String transactionDate);
}
