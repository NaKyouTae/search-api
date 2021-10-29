package com.kpsec.searchapi.model.result;

import lombok.Builder;
import lombok.Data;

/**
 * Create by na kyutae 2021-10-29.
 */
@Data
@Builder
public class AccountResult {

    /**
     * 연도
     */
    private int year;

    /**
     * 계좌명
     */
    private String accountName;

    /**
     * 계좌번호
     */
    private String accountNo;

    /**
     * 총 금액
     */
    private int sumAmt;
}

