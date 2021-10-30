package com.kpsec.searchapi.model.result;

import lombok.Builder;
import lombok.Data;

/**
 * 계좌 정보를 응답하기 위한 클래스
 * 
 * Create by na kyutae 2021-10-29.
 */
@Data
@Builder
public class NoAccountResult {

    /**
     * 연도
     */
    private int year;

    /**
     * 계좌명
     */
    private String name;

    /**
     * 계좌번호
     */
    private String acctNo;
}

