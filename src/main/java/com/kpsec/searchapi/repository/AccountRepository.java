package com.kpsec.searchapi.repository;

import com.kpsec.searchapi.model.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 계좌 Repository
 * Create by na kyutae 2021-10-29.
 */
public interface AccountRepository extends JpaRepository<AccountEntity, String> {
    List<AccountEntity> findByAccountNoNotIn(List<String> accountNos);
    AccountEntity findByAccountNo(String accountNo);
}
