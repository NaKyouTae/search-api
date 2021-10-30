package com.kpsec.searchapi.model.entity;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;

/**
 * 관리지점 관련 Entity
 *
 * Create by na kyutae 2021-10-29.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "KPSEC_TB_BRANCH")
public class BranchEntity {

    /**
     * 관리점 코드
     */
    @Id
    @NotNull
    private String branchCode;

    /**
     * 관리점 명
     */
    @NotNull
    private String branchName;

}
