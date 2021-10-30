package com.kpsec.searchapi.service.branch;


import com.kpsec.searchapi.common.exception.NotFoundException;
import org.springframework.stereotype.Component;

/**
 * 유효성 체크 관련 서비스 클래스
 *
 * Create By na kyutae 2021-10-31
 */
@Component
public class BranchValidationService {

    /**
     * 분당점이 판교점으로 통폐합하여 분당점 조회시 에러 발생
     *
     * @param branchName 관리지점 명
     * @return true or false
     */
    public boolean validationBranchName(String branchName) throws NotFoundException {
        if(branchName.equals("분당점")){
            throw new NotFoundException("br code not found error");
        }

        return false;
    }
}
