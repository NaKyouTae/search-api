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
     * 계좌번호
     */
    private String accountNo;

    /**
     * 계좌명
     */
    private String accountName;

    /**
     * 관리점 코드
     */
    private String branchCode;
}

