package com.kpsec.searchapi.model.result;

import lombok.Builder;
import lombok.Data;

/**
 * Create by na kyutae 2021-10-29.
 */
@Data
@Builder
public class ManagementPointResult {

    /**
     * 관리점 코드
     */
    private String managementPointCode;

    /**
     * 관리점 명
     */
    private String managementPointName;
}
