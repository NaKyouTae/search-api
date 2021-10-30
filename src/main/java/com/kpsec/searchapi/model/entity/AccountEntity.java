package com.kpsec.searchapi.model.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 계좌 관련 Entity
 *
 * Create by na kyutae 2021-10-29.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "KPSEC_TB_ACCOUNT")
public class AccountEntity {

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
