package com.kpsec.searchapi.model.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * 거래내역을 응답하기 위한 클래스
 * 
 * Create by na kyutae 2021-10-29.
 */
@Data
@Builder
@ApiModel
public class TransactionHistoryResult {

    /**
     * 거래일자
     */
    @ApiModelProperty(value = "거래 일자", required = true)
    private String transactionDate;

    /**
     * 계좌번호
     */
    @ApiModelProperty(value = "계좌 번호", required = true)
    private String accountNo;

    /**
     * 거래번호
     */
    @ApiModelProperty(value = "거래 번호", required = true)
    private String transactionNo;

    /**
     * 금액
     */
    @ApiModelProperty(value = "금액", required = true)
    private int amount;

    /**
     * 수수료
     */
    @ApiModelProperty(value = "수수료", required = true)
    private int commission;

    /**
     * 취소여부
     */
    @ApiModelProperty(value = "취소 여부", required = true)
    private boolean cancelYn;
}
