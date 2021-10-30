package com.kpsec.searchapi.repository;

import com.kpsec.searchapi.model.entity.BranchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 지점 Repository
 * Create by na kyutae 2021-10-29.
 */
public interface BranchRepository extends JpaRepository<BranchEntity, Long> {
    public BranchEntity findByBranchName(String branchCode);
}
