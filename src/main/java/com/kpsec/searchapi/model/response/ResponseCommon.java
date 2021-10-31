package com.kpsec.searchapi.model.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * Create By na kyutae 2021-10-31
 */
@Data
@Builder
@ApiModel
public class ResponseCommon<T> {
    /**
     * 응답 성공 여부
     */
    @ApiModelProperty(value = "응답 성공 여부", required = true)
    private String successed;

    /**
     * 응답 상태
     */
    @ApiModelProperty(value = "응답 상태", required = true)
    private HttpStatus status;
    
    /**
     * 응답 코드
     */
    @ApiModelProperty(value = "응답 코드", required = true)
    private int code;

    /**
     * 에러 메시지
     */
    @ApiModelProperty(value = "에러 메시지", required = false)
    private String errorMsg;

    /**
     * 결과
     */
    @ApiModelProperty(value = "결과", required = false)
    private T result;
}
