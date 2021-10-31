package com.kpsec.searchapi.model.entity;

import com.sun.istack.NotNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;

/**
 * 관리지점 관련 Entity
 *
 * Create by na kyutae 2021-10-29.
 */
@Data
@Entity
@Builder
@ApiModel
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "KPSEC_TB_BRANCH")
public class BranchEntity {

    /**
     * 관리점 코드
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "관리점 코드", required = true)
    private String branchCode;

    /**
     * 관리점 명
     */
    @NotNull
    @ApiModelProperty(value = "관리점 이름", required = true)
    private String branchName;

}
