package com.kpsec.searchapi.model.entity;

import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@Entity
@Builder
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "KPSEC_TB_ACCOUNT")
public class AccountEntity {

    /**
     * 계좌번호
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "계좌 번호", required = true)
    private String accountNo;

    /**
     * 계좌명
     */
    @ApiModelProperty(value = "계좌 이름", required = true)
    private String accountName;

    /**
     * 관리점 코드
     */
    @NotNull
    @ApiModelProperty(value = "관리점 코드", required = true)
    private String branchCode;
}
