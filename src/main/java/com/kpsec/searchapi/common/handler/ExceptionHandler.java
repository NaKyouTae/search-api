package com.kpsec.searchapi.common.handler;

import com.kpsec.searchapi.common.exception.NotFoundException;
import com.kpsec.searchapi.model.response.ResponseCommon;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Exception Handler
 *
 * Create By na kyutae 2021-10-31
 */
@RestControllerAdvice(annotations = RestController.class)
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {NotFoundException.class})
    protected ResponseCommon<Object> handleCustomException(NotFoundException e) {
        return ResponseCommon.builder().code(Response.SC_NOT_FOUND).result(null).successed("false").status(HttpStatus.NOT_FOUND).errorMsg(e.getMessage()).build();
    }
}
