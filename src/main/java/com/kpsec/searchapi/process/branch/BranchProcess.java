package com.kpsec.searchapi.process.branch;

import com.kpsec.searchapi.model.entity.BranchEntity;
import com.kpsec.searchapi.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 관리지점 관련해서 DB와 통신하는 클래스
 * Create By na kyutae 2021-10-30
 */
@Component
@RequiredArgsConstructor
public class BranchProcess {
    private final BranchRepository branchRepository;

    public List<BranchEntity> getBranchAll() {
        return branchRepository.findAll();
    }

    /**
     * 지점 명으로 지점 정보 조회하는 메서드
     * 
     * @param branchName 지점 명
     * @return BranchEntity 지점 정보
     */
    public BranchEntity getBranchBybranchName(String branchName) {
        return branchRepository.findByBranchName(branchName);
    }

}
