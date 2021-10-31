package com.kpsec.searchapi.model.entity;

import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 거래내역 관련 Entity
 *
 * Create by na kyutae 2021-10-29.
 */
@Data
@Entity
@Builder
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "KPSEC_TB_TRANSACTION_HISTORY")
public class TransactionHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 거래일자
     */
    @NotNull
    @ApiModelProperty(value = "거래 일자", required = true)
    private String transactionDate;

    /**
     * 계좌번호
     */
    @NotNull
    @ApiModelProperty(value = "계좌 번호", required = true)
    private String accountNo;

    /**
     * 거래번호
     */
    @NotNull
    @ApiModelProperty(value = "거래 번호", required = true)
    private String transactionNo;

    /**
     * 금액
     */
    @NotNull
    @ApiModelProperty(value = "금액", required = true)
    private int amount;

    /**
     * 수수료
     */
    @NotNull
    @ApiModelProperty(value = "수수료", required = true)
    private int commission;

    /**
     * 취소여부
     */
    @NotNull
    @ApiModelProperty(value = "거래 취소 여부", required = true)
    private boolean cancelYn;
}
