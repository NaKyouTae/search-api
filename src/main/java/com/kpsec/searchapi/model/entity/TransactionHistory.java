package com.kpsec.searchapi.model.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * Create by na kyutae 2021-10-29.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "KP_TB_TRANSACTION_HISTORY")
public class TransactionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 거래일자
     */
    @NotNull
    private String transactionDate;

    /**
     * 계좌번호
     */
    @NotNull
    private String accountNo;

    /**
     * 거래번호
     */
    @NotNull
    private String transactionNo;
    /**
     * 금액
     */
    @NotNull
    private int amount;

    /**
     * 수수료
     */
    @NotNull
    private int commission;

    /**
     * 취소여부
     */
    @NotNull
    private boolean cancelYn;
}
