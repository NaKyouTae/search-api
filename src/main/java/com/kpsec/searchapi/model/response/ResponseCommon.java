package com.kpsec.searchapi.model.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * Create By na kyutae 2021-10-31
 */
@Data
@Builder
public class ResponseCommon<T> {
    /**
     * 응답 성공 여부
     */
    private String successed;

    /**
     * 응답 상태
     */
    private HttpStatus status;
    
    /**
     * 응답 코드
     */
    private int code;

    /**
     * 에러 메시지
     */
    private String errorMsg;

    /**
     * 결과
     */
    private T result;
}
