package com.kpsec.searchapi.model.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Create by na kyutae 2021-10-29.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "KP_TB_ACCOUNT")
public class Account {

    /**
     * 계좌번호
     */
    @Id
    @NotNull
    private String accountNo;

    /**
     * 계좌명
     */
    private String accountName;

    /**
     * 관리점 코드
     */
    @NotNull
    private String branchCode;
}
