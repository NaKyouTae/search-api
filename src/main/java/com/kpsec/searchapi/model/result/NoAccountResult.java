package com.kpsec.searchapi.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 계좌 정보를 응답하기 위한 클래스
 * 
 * Create by na kyutae 2021-10-29.
 */
@Data
@Builder
@ApiModel
public class NoAccountResult {

    /**
     * 연도
     */
    @ApiModelProperty(value = "연도", required = true)
    private int year;

    /**
     * 계좌명
     */
    @ApiModelProperty(value = "계좌 이름", required = true)
    private String name;

    /**
     * 계좌번호
     */
    @ApiModelProperty(value = "계좌 번호", required = true)
    private String acctNo;
}

