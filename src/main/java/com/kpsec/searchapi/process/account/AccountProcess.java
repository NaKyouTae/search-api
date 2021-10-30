package com.kpsec.searchapi.process.account;

import com.kpsec.searchapi.model.entity.AccountEntity;
import com.kpsec.searchapi.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 계좌 관련해서 DB와 통신하기 위한 클래스
 * 
 * Create By na kyutae 2021-10-30
 */
@Component
@RequiredArgsConstructor
public class AccountProcess {

    private final AccountRepository accountRepository;

    /**
     * 전체 계좌 조회 메서드
     * 
     * @return List<AccountEntity> 계좌 정보 리스트
     */
    public List<AccountEntity> getAccountAll() {
        return accountRepository.findAll();
    }

    /**
     * 거래 내역이 없는 계좌 정보 조회 메서드
     * 
     * @param accounts 계좌번호 리스트
     * @return List<AccountEntity> 계좌 정보 리스트
     */
    public List<AccountEntity> getAccountNoHistory(List<String> accounts) {
        return accountRepository.findByAccountNoNotIn(accounts);
    }

    /**
     * 계좌 번호를 이용하여 계좌 정보 조회 메서드
     *
     * @param accountNo 계좌번호
     * @return AccountEntity 계좌 정보
     */
    public AccountEntity getAccount(String accountNo) {
        return accountRepository.findByAccountNo(accountNo);
    }
}
