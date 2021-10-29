package com.kpsec.searchapi.model.entity;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Data;

/**
 * Create by na kyutae 2021-10-29.
 */
@Data
@Builder
public class ManagementPoint {
    /**
     * 관리점 코드
     */
    @NotNull
    private String managementPointCode;

    /**
     * 관리점 명
     */
    @NotNull
    private String managementPointName;
}
