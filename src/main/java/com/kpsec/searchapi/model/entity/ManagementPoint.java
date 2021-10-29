package com.kpsec.searchapi.model.entity;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Create by na kyutae 2021-10-29.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ManagementPoint {

    /**
     * 관리점 코드
     */
    @Id
    @NotNull
    private String managementPointCode;

    /**
     * 관리점 명
     */
    @NotNull
    private String managementPointName;

}
