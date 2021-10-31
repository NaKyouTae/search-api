package com.kpsec.searchapi.controller.account;

import com.kpsec.searchapi.model.response.ResponseCommon;
import com.kpsec.searchapi.model.result.AccountResult;
import com.kpsec.searchapi.model.result.NoAccountResult;
import com.kpsec.searchapi.service.account.AccountService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 계좌 정보 관련 컨트롤러
 *
 * Create By na kyutae 2021-10-31
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
@Api(tags = "계좌정보 관련 컨트롤러")
public class AccountController {

    private final AccountService accountService;

    /**
     * 요청 받은 연도를 기준으로 거래 금액이 가장 큰 계좌를 조회 메서드
     * 
     * @param years 연도 리스트 ["2018", "2019" ...]
     * @return ResponseCommon<List<AccountResult>> 연도별 거래금액이 가장 큰 계좌 리스트
     */
    @ApiOperation(value = "Get", notes = "요청 받은 연도를 기준으로 거래 금액이 가장 큰 계좌를 조회", response = ResponseCommon.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "200 OK"),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR")
    })
    @GetMapping("/histories-years-top")
    public ResponseCommon<List<AccountResult>> getAccountOfTopForYear(@ApiParam(value = "연도 리스트", required = true) @RequestParam(required = true) List<String> years) {
        List<AccountResult> accountResult = accountService.getAccountOfTopForYear(years);

        try {
            return ResponseCommon.<List<AccountResult>>builder()
                    .status(HttpStatus.OK)
                    .code(Response.SC_OK)
                    .successed("true")
                    .errorMsg(null)
                    .result(accountResult)
                    .build();
        }catch (Exception e) {
            return ResponseCommon.<List<AccountResult>>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .code(Response.SC_INTERNAL_SERVER_ERROR)
                    .successed("false")
                    .errorMsg(e.getMessage())
                    .result(null)
                    .build();
        }
    }

    /**
     * 연도별 거래내역이 없는 계좌 조회 메서드
     *
     * @param years 연도 리스트 ["2018", "2019" ...]
     * @return ResponseCommon<List<AccountResult>> 거래내역이 없는 계좌 리스트
     */
    @ApiOperation(value = "Get", notes = "연도별 거래내역이 없는 계좌 조회", response = ResponseCommon.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "200 OK"),
            @ApiResponse(code = 500, message = "INTERNAL_SERVER_ERROR")
    })
    @GetMapping("/histories-years-none")
    public ResponseCommon<List<NoAccountResult>> getNoHistoryAccountForYear(@ApiParam(value = "연도 리스트", required = true) @RequestParam(required = true) List<String> years) {
        List<NoAccountResult> accountResult = accountService.getNoHistoryAccountForYear(years);

        try {
            return ResponseCommon.<List<NoAccountResult>>builder()
                    .status(HttpStatus.OK)
                    .code(Response.SC_OK)
                    .successed("true")
                    .errorMsg(null)
                    .result(accountResult)
                    .build();
        }catch (Exception e) {
            return ResponseCommon.<List<NoAccountResult>>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .code(Response.SC_INTERNAL_SERVER_ERROR)
                    .successed("false")
                    .errorMsg(e.getMessage())
                    .result(null)
                    .build();
        }
    }
}