package com.kpsec.searchapi.model.result;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 관리지점을 응답하기 위한 클래스
 * 
 * Create by na kyutae 2021-10-29.
 */
@Data
@Builder
public class BranchResult {

    /**
     * 연도
     */
    private int year;

    /**
     * 관리점 목록
     */
    private List<Branch> dataList;

    @Data
    @Builder
    public static class Branch {
        /**
         * 관리점 코드
         */
        private String brCode;

        /**
         * 관리점 명
         */
        private String brName;

        /**
         * 총 금액
         */
        private int sumAmt;
    }
}
