package com.kpsec.searchapi.service.account;

import com.kpsec.searchapi.model.entity.AccountEntity;
import com.kpsec.searchapi.model.entity.TransactionHistoryEntity;
import com.kpsec.searchapi.model.result.AccountResult;
import com.kpsec.searchapi.model.result.NoAccountResult;
import com.kpsec.searchapi.process.account.AccountProcess;
import com.kpsec.searchapi.process.history.TransactionHistoryProcess;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 계좌의 계좌 관련 서비스 클래스
 * 
 * Create By na kyutae 2021-10-29
 */
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountProcess accountProcess;
    private final TransactionHistoryProcess transactionHistoryProcess;

    /**
     * 내림차순으로 거래 금액 정렬
     */
    private final Comparator<AccountResult> sumAmtDescCompare = new Comparator<>() {
        @Override
        public int compare(AccountResult o1, AccountResult o2) {
            return Integer.compare(o2.getSumAmt(), o1.getSumAmt());
        }
    };

    /**
     * 요청 받은 연도별로 가장 거래 금액이 큰 계좌를 조회하는 메서드
     *
     * @param years ex. ["2018", "2019", "2020"]
     *              - 1900년대, 2000년대를 기준으로 유효성을 체크한다.
     *              - 연도는 yyyy 형식으로 체크되며 유효성이 확인되지 않을 시 해당 연도는 결과를 반환하지 않는다.
     * @return List<AccountResult> 오름차순 연도의 계좌 리스트
     *     
     */
    public List<AccountResult> getAccountOfTopForYear(List<String> years) {
        List<AccountEntity> accounts = accountProcess.getAccountAll();

        List<AccountResult> topList = new ArrayList<>();

        years.forEach(year -> {
            if(!Pattern.matches("(19|20)\\d{2}", year)) return;

            List<AccountResult> accountResults = new ArrayList<>();

            accounts.forEach(account -> {
                List<TransactionHistoryEntity> history = transactionHistoryProcess.getHistory(account.getAccountNo(), year);

                int sumAmt = history.stream().mapToInt(TransactionHistoryEntity::getAmount).sum();
                int commission = history.stream().mapToInt(TransactionHistoryEntity::getCommission).sum();

                int totalAmt = sumAmt - commission;

                if(totalAmt == 0) return;

                AccountResult accountResult = AccountResult.builder()
                        .acctNo(account.getAccountNo())
                        .name(account.getAccountName())
                        .sumAmt(totalAmt)
                        .year(Integer.parseInt(year))
                        .build();

                accountResults.add(accountResult);
            });

            if(accountResults.size() > 0) {
                topList.add(accountResults.stream().sorted(sumAmtDescCompare).collect(Collectors.toList()).get(0));
            }
        });

        return topList.stream().sorted(new Comparator<>() {
            @Override
            public int compare(AccountResult o1, AccountResult o2) {
                return Integer.compare(o1.getYear(), o2.getYear());
            }
        }).collect(Collectors.toList());
    }

    /**
     * 요청 받은 연도별 거래 내역이 없는 계좌을 조회하는 메서드
     * 
     * @param years ex. ["2018", "2019", "2020"]
     *              - 1900년대, 2000년대를 기준으로 유효성을 체크한다.
     *              - 연도는 yyyy 형식으로 체크되며 유효성이 확인되지 않을 시 해당 연도는 결과를 반환하지 않는다.
     * @return List<NoAccountResult> 오름차순 연도의 객 리스트
     */
    public List<NoAccountResult> getNoHistoryAccountForYear(List<String> years) {
        List<NoAccountResult> accountResults = new ArrayList<>();

        years.forEach(year -> {
            if(!Pattern.matches("(19|20)\\d{2}", year)) return;

            List<TransactionHistoryEntity> transactionHistories = transactionHistoryProcess.getHistory(year);

            List<String> accountNos = transactionHistories.stream().map(TransactionHistoryEntity::getAccountNo).distinct().collect(Collectors.toList());
            List<AccountEntity> noHistoryAccount = accountProcess.getAccountNoHistory(accountNos);

            noHistoryAccount.forEach(account -> {
                NoAccountResult accountResult = NoAccountResult.builder()
                        .acctNo(account.getAccountNo())
                        .name(account.getAccountName())
                        .year(Integer.parseInt(year))
                        .build();

                accountResults.add(accountResult);
            });
        });

        return accountResults.stream().sorted(new Comparator<>() {
            @Override
            public int compare(NoAccountResult o1, NoAccountResult o2) {
                return Integer.compare(o1.getYear(), o2.getYear());
            }
        }).collect(Collectors.toList());
    }
}
