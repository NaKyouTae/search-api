package com.kpsec.searchapi.model.result;

import lombok.Builder;
import lombok.Data;

/**
 * Create by na kyutae 2021-10-29.
 */
@Data
@Builder
public class TransactionHistoryResult {

    /**
     * 거래일자
     */
    private String transactionDate;

    /**
     * 계좌번호
     */
    private String accountNo;

    /**
     * 거래번호
     */
    private String transactionNo;

    /**
     * 금액
     */
    private int amount;

    /**
     * 수수료
     */
    private int commission;

    /**
     * 취소여부
     */
    private boolean cancelYn;
}
