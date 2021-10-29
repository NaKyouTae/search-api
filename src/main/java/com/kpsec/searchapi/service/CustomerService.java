package com.kpsec.searchapi.service;

import com.kpsec.searchapi.model.entity.Account;
import com.kpsec.searchapi.model.entity.TransactionHistory;
import com.kpsec.searchapi.model.result.AccountResult;
import com.kpsec.searchapi.repository.AccountRepository;
import com.kpsec.searchapi.repository.TransactionHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final AccountRepository accountRepository;
    private final TransactionHistoryRepository transactionHistoryRepository;

    public List<AccountResult> getHistory() {
        List<Account> accounts = accountRepository.findAll();

        List<AccountResult> list2018 = new ArrayList<>();
        List<AccountResult> list2019 = new ArrayList<>();

        accounts.forEach(account -> {
            List<TransactionHistory> history2018 = transactionHistoryRepository.findByAccountNoAndTransactionDateStartingWithAndCancelYnFalse(account.getAccountNo(), "2018");
            List<TransactionHistory> history2019 = transactionHistoryRepository.findByAccountNoAndTransactionDateStartingWithAndCancelYnFalse(account.getAccountNo(), "2019");

            int max2018 = history2018.stream().mapToInt(TransactionHistory::getAmount).sum();
            int max2019 = history2019.stream().mapToInt(TransactionHistory::getAmount).sum();

            AccountResult accountMax2018 = AccountResult.builder()
                    .accountNo(account.getAccountNo())
                    .accountName(account.getAccountName())
                    .sumAmt(max2018)
                    .year(2018)
                    .build();

            AccountResult accountMax2019 = AccountResult.builder()
                    .accountNo(account.getAccountNo())
                    .accountName(account.getAccountName())
                    .sumAmt(max2019)
                    .year(2019)
                    .build();

            list2018.add(accountMax2018);
            list2019.add(accountMax2019);
        });

        List<AccountResult> accountResults = new ArrayList<>();

        Comparator<AccountResult> compare = new Comparator<AccountResult>() {
            @Override
            public int compare(AccountResult o1, AccountResult o2) {
                return Integer.compare(o2.getSumAmt(), o1.getSumAmt());
            }
        };

        accountResults.add(list2018.stream().sorted(compare).collect(Collectors.toList()).get(0));
        accountResults.add(list2019.stream().sorted(compare).collect(Collectors.toList()).get(0));

        return accountResults;
    }
}
