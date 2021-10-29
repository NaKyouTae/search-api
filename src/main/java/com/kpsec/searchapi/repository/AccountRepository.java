package com.kpsec.searchapi.repository;

import com.kpsec.searchapi.model.entity.Account;
import com.kpsec.searchapi.model.result.AccountResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Create by na kyutae 2021-10-29.
 */
public interface AccountRepository extends JpaRepository<Account, String> {
}
