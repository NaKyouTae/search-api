package com.kpsec.searchapi.repository;

import com.kpsec.searchapi.model.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Create by na kyutae 2021-10-29.
 */
public interface AccountRepository extends JpaRepository<Account, String> {
}
