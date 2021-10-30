package com.kpsec.searchapi.repository;

import com.kpsec.searchapi.model.entity.TransactionHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 거래내역 Repository
 *
 * Create by na kyutae 2021-10-29.
 */
public interface TransactionHistoryRepository extends JpaRepository<TransactionHistoryEntity, Long> {
    List<TransactionHistoryEntity> findByAccountNoAndTransactionDateStartingWithAndCancelYnFalse(String accountNo, String transactionDate);
    List<TransactionHistoryEntity> findByTransactionDateStartingWithAndCancelYnFalse(String transactionDate);
}
