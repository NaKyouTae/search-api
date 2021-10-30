package com.kpsec.searchapi.controller.branch;

import com.kpsec.searchapi.common.exception.NotFoundException;
import com.kpsec.searchapi.model.response.ResponseCommon;
import com.kpsec.searchapi.model.result.BranchResult;
import com.kpsec.searchapi.service.branch.BranchService;
import com.kpsec.searchapi.service.branch.BranchValidationService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 관리지점 관련한 컨트롤러
 * Create By na kyutae 2021-10-31
 */
@RestController
@RequestMapping("/branches")
@RequiredArgsConstructor
public class BranchController {

    private final BranchService branchService;
    private final BranchValidationService branchValidationService;

    /**
     * 연도별, 관리점 별 거래 금액의 순서를 조회하는 메서드
     *
     * @return List<BranchResult> 지점 리스트
     */
    @GetMapping("amounts")
    public ResponseCommon<List<BranchResult>> getBranchOfTopForYear() {
        List<BranchResult> branchResults = branchService.getBranchOfTopForYear();

        try{
            return ResponseCommon.<List<BranchResult>>builder()
                    .status(HttpStatus.OK)
                    .code(Response.SC_OK)
                    .successed("true")
                    .result(branchResults)
                    .build();
        }catch (Exception e) {
            return ResponseCommon.<List<BranchResult>>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .code(Response.SC_INTERNAL_SERVER_ERROR)
                    .successed("false")
                    .errorMsg(e.getMessage())
                    .result(null)
                    .build();
        }
    }

    /**
     * 지점명을 입력 받아 해당 지점의 총 거래금액을 조회하는 메서드
     *
     * @param branchName 지점명
     * @return BranchResult.Branch 지점의 정보 및 총 거래금액
     */
    @GetMapping("/names")
    public ResponseCommon<BranchResult.Branch> getBranchSumAmt(@RequestParam String branchName) {
        branchValidationService.validationBranchName(branchName);

        BranchResult.Branch branch = branchService.getBranchSumAmt(branchName);

        try{
            return ResponseCommon.<BranchResult.Branch>builder()
                    .status(HttpStatus.OK)
                    .code(Response.SC_OK)
                    .successed("true")
                    .result(branch)
                    .build();
        }catch (NotFoundException e) {
            return ResponseCommon.<BranchResult.Branch>builder()
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .code(Response.SC_INTERNAL_SERVER_ERROR)
                    .successed("false")
                    .errorMsg(e.getMessage())
                    .result(null)
                    .build();
        }
    }
}
