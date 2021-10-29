package com.kpsec.searchapi.service;

import com.kpsec.searchapi.model.entity.Account;
import com.kpsec.searchapi.model.entity.TransactionHistory;
import com.kpsec.searchapi.model.result.AccountResult;
import com.kpsec.searchapi.repository.AccountRepository;
import com.kpsec.searchapi.repository.TransactionHistoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;


//@SpringBootTest
//@TestPropertySource("classpath:application.yml")
@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @InjectMocks
    private CustomerService customerService;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionHistoryRepository transactionHistoryRepository;
    @Mock
    private List<Account> accounts;
    @Mock
    private List<TransactionHistory> histories2018;
    @Mock
    private List<TransactionHistory> histories2019;
    @Mock
    private AccountResult accountResult;
//    @Captor
//    private ArgumentCaptor<TransactionHistory> accountNoCaptor;

    @BeforeEach
    public void getHistoryBefore() throws IOException {
        Resource resourceOne = new ClassPathResource("static/data/계좌정보.csv");
        accounts = Files.readAllLines(resourceOne.getFile().toPath(), StandardCharsets.UTF_8)
                .stream().skip(1).map(line -> {
                    String[] split = line.split(",");
                    return Account.builder()
                            .accountNo(split[0])
                            .accountName(split[1])
                            .branchCode(split[2])
                            .build();
                }).collect(Collectors.toList());

        Resource resourceTwo = new ClassPathResource("static/data/거래내역.csv");
        histories2018 = Files.readAllLines(resourceTwo.getFile().toPath(), StandardCharsets.UTF_8)
                .stream()
                .skip(1)
                .filter(line -> line.split(",")[5].equals("N") && line.split(",")[0].startsWith("2018"))
                .map(line -> {
                    String[] split = line.split(",");

                    return TransactionHistory.builder()
                            .transactionDate(split[0])
                            .accountNo(split[1])
                            .transactionNo(split[2])
                            .amount(Integer.parseInt(split[3]))
                            .commission(Integer.parseInt(split[4]))
                            .cancelYn(split[5].equals("Y"))
                            .build();
                }).collect(Collectors.toList());

        histories2019 = Files.readAllLines(resourceTwo.getFile().toPath(), StandardCharsets.UTF_8)
                .stream()
                .skip(1)
                .filter(line -> line.split(",")[5].equals("N") && line.split(",")[0].startsWith("2019"))
                .map(line -> {
                    String[] split = line.split(",");

                    return TransactionHistory.builder()
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
    @DisplayName("2018 또는 2019년도 거래 금액이 제일 큰 계좌 조회")
    public void getHistory() throws IOException {
        Mockito.when(accountRepository.findAll()).thenReturn(accounts);

        ArgumentCaptor<String> accountNoCaptor = ArgumentCaptor.forClass(String.class);

        Mockito.verify(transactionHistoryRepository).findByAccountNoAndTransactionDateStartingWithAndCancelYnFalse(accountNoCaptor.capture(), eq("2018"));

        List<AccountResult> res = customerService.getHistory();

        Mockito.when(transactionHistoryRepository.findByAccountNoAndTransactionDateStartingWithAndCancelYnFalse(any(), eq("2018"))).thenReturn(histories2018);
        System.out.println(accountNoCaptor.getValue());

        Mockito.verify(transactionHistoryRepository).findByAccountNoAndTransactionDateStartingWithAndCancelYnFalse(accountNoCaptor.capture(), eq("2019"));
        Mockito.when(transactionHistoryRepository.findByAccountNoAndTransactionDateStartingWithAndCancelYnFalse(accountNoCaptor.getValue(), eq("2019"))).thenReturn(histories2019);

//        Mockito.verify(accountRepository).findAll();

        List<AccountResult> accountResults = new ArrayList<>();

        accountResults.add(AccountResult.builder().year(2018).accountName("테드").accountNo("11111114").sumAmt(117000000).build());
        accountResults.add(AccountResult.builder().year(2019).accountName("리노").accountNo("11111113").sumAmt(84700000).build());

        Assertions.assertArrayEquals(accountResults.toArray(), res.toArray());
    }
}
