package com.kpsec.searchapi.service;

import com.kpsec.searchapi.model.entity.AccountEntity;
import com.kpsec.searchapi.model.entity.TransactionHistoryEntity;
import com.kpsec.searchapi.model.result.AccountResult;
import com.kpsec.searchapi.model.result.NoAccountResult;
import com.kpsec.searchapi.process.account.AccountProcess;
import com.kpsec.searchapi.process.history.TransactionHistoryProcess;
import com.kpsec.searchapi.service.account.AccountService;
import com.kpsec.searchapi.util.DistinctUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.eq;


@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;
    @Mock
    private AccountProcess accountProcess;
    @Mock
    private TransactionHistoryProcess transactionHistoryProcess;
    @Mock
    private List<AccountEntity> accounts;
    @Mock
    private List<TransactionHistoryEntity> histories;
    @Mock
    private AccountResult accountResult;

    @BeforeEach
    public void getHistoryBefore() throws IOException {
        Resource accountResource = new ClassPathResource("static/data/계좌정보.csv");
        accounts = Files.readAllLines(accountResource.getFile().toPath(), StandardCharsets.UTF_8)
                .stream().skip(1).map(line -> {
                    String[] split = line.split(",");
                    return AccountEntity.builder()
                            .accountNo(split[0])
                            .accountName(split[1])
                            .branchCode(split[2])
                            .build();
                }).collect(Collectors.toList());

        Resource historyResource = new ClassPathResource("static/data/거래내역.csv");
        histories = Files.readAllLines(historyResource.getFile().toPath(), StandardCharsets.UTF_8)
                .stream()
                .skip(1)
                .filter(line -> line.split(",")[5].equals("N"))
                .map(line -> {
                    String[] split = line.split(",");

                    return TransactionHistoryEntity.builder()
                            .transactionDate(split[0])
                            .accountNo(split[1])
                            .transactionNo(split[2])
                            .amount(Integer.parseInt(split[3]))
                            .commission(Integer.parseInt(split[4]))
                            .cancelYn(split[5].equals("Y"))
                            .build();
                }).collect(Collectors.toList());
    }

    @Test
    @Order(1)
    @DisplayName("2018년도 또는 2019년도 거래 금액이 제일 큰 계좌 조회")
    public void getAccountOfBestForYear() throws IOException {

        // 조회 년도
        List<String> years = new ArrayList<>();

        years.add("2019");
        years.add("2018");

        // when return
        Mockito.when(accountProcess.getAccountAll()).thenReturn(accounts);

        years.forEach(year -> {
            accounts.forEach(account -> {
                List<TransactionHistoryEntity> accountHistory = histories.stream().filter(item -> item.getAccountNo().equals(account.getAccountNo()) && item.getTransactionDate().startsWith(year)).collect(Collectors.toList());
                Mockito.when(transactionHistoryProcess.getHistory(eq(account.getAccountNo()), eq(year))).thenReturn(accountHistory);
            });
        });

        // when 실행
        List<AccountResult> res = accountService.getAccountOfTopForYear(years);

        // 예상 결과
        List<AccountResult> accountResults = new ArrayList<>();

        accountResults.add(AccountResult.builder().year(2018).name("테드").acctNo("11111114").sumAmt(28992000).build());
        accountResults.add(AccountResult.builder().year(2019).name("에이스").acctNo("11111112").sumAmt(40998400).build());

        // 예측 결과와 실제 결과 비교
        Assertions.assertArrayEquals(accountResults.toArray(), res.toArray());
    }

    @Test
    @Order(2)
    @DisplayName("2018년도 또는 2019년도 거래가 없는 계좌 조회")
    public void getNoHistoryAccount() {

        // 조회 년도
        List<String> years = new ArrayList<>();

        years.add("2019");
        years.add("2018");

        // when return
        years.forEach(year -> {
            List<TransactionHistoryEntity> accountHistory = histories.stream().filter(item -> item.getTransactionDate().startsWith(year)).collect(Collectors.toList());
            Mockito.when(transactionHistoryProcess.getHistory(eq(year))).thenReturn(accountHistory);

            List<TransactionHistoryEntity> distinctHistory = accountHistory.stream().filter(DistinctUtil.distinctByKey(history -> history.getAccountNo())).collect(Collectors.toList());

            List<AccountEntity> noHistoryAccount = new ArrayList<>();

            accounts.stream().map(account -> {
                long count = distinctHistory.stream().filter(item -> item.getAccountNo().equals(account.getAccountNo())).count();

                if(count == 0) {
                    noHistoryAccount.add(account);
                }

                return null;
            }).collect(Collectors.toList());

            List<String> accountNos = distinctHistory.stream().map(TransactionHistoryEntity::getAccountNo).collect(Collectors.toList());

            Mockito.when(accountProcess.getAccountNoHistory(eq(accountNos))).thenReturn(noHistoryAccount);
        });

        // when 실행
        List<NoAccountResult> res = accountService.getNoHistoryAccountForYear(years);

        // 예상 결과
        List<NoAccountResult> accountResults = new ArrayList<>();

        accountResults.add(NoAccountResult.builder().year(2018).name("사라").acctNo("11111115").build());
        accountResults.add(NoAccountResult.builder().year(2018).name("제임스").acctNo("11111118").build());
        accountResults.add(NoAccountResult.builder().year(2018).name("에이스").acctNo("11111121").build());
        accountResults.add(NoAccountResult.builder().year(2019).name("테드").acctNo("11111114").build());
        accountResults.add(NoAccountResult.builder().year(2019).name("제임스").acctNo("11111118").build());
        accountResults.add(NoAccountResult.builder().year(2019).name("에이스").acctNo("11111121").build());

        // 예측 결과와 실제 결과 비교
        Assertions.assertArrayEquals(accountResults.toArray(), res.toArray());
    }
}
