package com.kpsec.searchapi.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel
public class BranchResult {

    /**
     * 연도
     */
    @ApiModelProperty(value = "연도", required = true)
    private int year;

    /**
     * 관리점 목록
     */
    @ApiModelProperty(value = "관리점 목록", required = true)
    private List<Branch> dataList;

    @Data
    @Builder
    @ApiModel
    public static class Branch {
        /**
         * 관리점 코드
         */
        @ApiModelProperty(value = "관리점 코드", required = true)
        private String brCode;

        /**
         * 관리점 명
         */
        @ApiModelProperty(value = "관리점 이름", required = true)
        private String brName;

        /**
         * 총 금액
         */
        @ApiModelProperty(value = "총 금액", required = true)
        private int sumAmt;
    }
}
