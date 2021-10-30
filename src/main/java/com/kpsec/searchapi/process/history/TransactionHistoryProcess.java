package com.kpsec.searchapi.process.history;

import com.kpsec.searchapi.model.entity.TransactionHistoryEntity;
import com.kpsec.searchapi.repository.TransactionHistoryRepository;
import com.kpsec.searchapi.util.DistinctUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 거래내역 관련해서 DB와 통신하는 클래스
 * 
 * Create By na kyutae 2021-10-30
 */
@Component
@RequiredArgsConstructor
public class TransactionHistoryProcess {

    private final TransactionHistoryRepository transactionHistoryRepository;

    /**
     * 전체 거래내역 조회 메서드
     * 
     * @return List<TransactionHistoryEntity> 거래내역 리스트
     */
    public List<TransactionHistoryEntity> getHistoryAll() {
        return transactionHistoryRepository.findAll();
    }

    /**
     * 계좌번호와 거래일자(yyyy)를 받아 거래 내역을 조회하는 메서드
     * 단, 취소된 거래는 포함하지 않는다.
     *
     * @param accountNo 계좌번호
     * @param transactionDate 거래일자(yyyy)
     * @return List<TransactionHistoryEntity> 거래내역 리스트
     */
    public List<TransactionHistoryEntity> getHistory(String accountNo, String transactionDate) {
        return transactionHistoryRepository.findByAccountNoAndTransactionDateStartingWithAndCancelYnFalse(accountNo, transactionDate);
    }

    /**
     * 거래일자(yyyy)를 받아 거래내역을 조회하는 메서드
     * 단, 취소된 거래는 포함하지 않는다.
     * 
     * @param transactionDate 거래일자(yyyy)
     * @return List<TransactionHistoryEntity> 거래내역 리스트
     */
    public List<TransactionHistoryEntity> getHistory(String transactionDate) {
        return transactionHistoryRepository.findByTransactionDateStartingWithAndCancelYnFalse(transactionDate);
    }

    /**
     * 거래 내역의 연도(yyyy)를 중복제거하여 리스트로 반환하는 메서드
     * 
     * @return List<String> 연도(yyyy) 리스트
     */
    public List<String> getHistoryDistinctYear() {
        List<TransactionHistoryEntity> histories = getHistoryAll();;

        List<TransactionHistoryEntity> distinctHistories = histories.stream().filter(DistinctUtil.distinctByKey(history -> {
            return history.getTransactionDate().substring(0, 4);
        })).collect(Collectors.toList());

        return distinctHistories.stream().map(history -> history.getTransactionDate().substring(0, 4)).collect(Collectors.toList());
    }
}
