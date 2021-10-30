package com.kpsec.searchapi.service;

import com.kpsec.searchapi.model.entity.AccountEntity;
import com.kpsec.searchapi.model.entity.BranchEntity;
import com.kpsec.searchapi.model.entity.TransactionHistoryEntity;
import com.kpsec.searchapi.model.result.BranchResult;
import com.kpsec.searchapi.process.account.AccountProcess;
import com.kpsec.searchapi.process.branch.BranchProcess;
import com.kpsec.searchapi.process.history.TransactionHistoryProcess;
import com.kpsec.searchapi.service.branch.BranchService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Create By na kyutae 2021-10-30
 */
@ExtendWith(MockitoExtension.class)
public class BranchServiceTest {

    @InjectMocks
    private BranchService branchService;

    @Mock
    private BranchProcess branchProcess;
    @Mock
    private AccountProcess accountProcess;
    @Mock
    private TransactionHistoryProcess transactionHistoryProcess;
    @Mock
    private List<AccountEntity> accounts;
    @Mock
    private List<TransactionHistoryEntity> histories;
    @Mock
    private List<BranchEntity> branches;

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

        Resource branchResource = new ClassPathResource("static/data/관리점정보.csv");
        branches = Files.readAllLines(branchResource.getFile().toPath(), StandardCharsets.UTF_8)
                .stream()
                .skip(1)
                .map(line -> {
                   String[] split = line.split(",");

                   return BranchEntity.builder()
                           .branchCode(split[0])
                           .branchName(split[1])
                           .build();
                }).collect(Collectors.toList());
    }

    @Test
    @Order(1)
    @DisplayName("연도별 관리점별 거래 금액 합계의 순서 조회")
    public void getBranchOfTopForYear() {

        List<String> years = new ArrayList<>();

        histories.forEach(history -> {
            String year = history.getTransactionDate().substring(0, 4);
            if(!years.contains(year)) {
                years.add(year);
            }
        });

        // when return
        Mockito.when(branchProcess.getBranchAll()).thenReturn(branches);
        Mockito.when(transactionHistoryProcess.getHistoryDistinctYear()).thenReturn(years);

        years.forEach(year -> {
            List<TransactionHistoryEntity> accountHistory = histories.stream().filter(item -> item.getTransactionDate().startsWith(year)).collect(Collectors.toList());
            Mockito.when(transactionHistoryProcess.getHistory(year)).thenReturn(accountHistory);
            branches.forEach(branch -> {
                histories.forEach(history -> {

                    AccountEntity accountInfo = accounts.stream().filter(account -> account.getAccountNo().equals(history.getAccountNo())).collect(Collectors.toList()).get(0);

                    Mockito.when(accountProcess.getAccount(history.getAccountNo())).thenReturn(accountInfo);
                });
            });
        });

        // when 실행
        List<BranchResult> branchResults = branchService.getBranchOfTopForYear();
        
        // 예상 결과
        List<BranchResult> expectBranchResultList = new ArrayList<>();

        List<BranchResult.Branch> branchList2018 = new ArrayList<>();
        branchList2018.add(BranchResult.Branch.builder().brCode("B").brName("분당점").sumAmt(38500000).build());
        branchList2018.add(BranchResult.Branch.builder().brCode("A").brName("판교점").sumAmt(20510000).build());
        branchList2018.add(BranchResult.Branch.builder().brCode("C").brName("강남점").sumAmt(20234567).build());
        branchList2018.add(BranchResult.Branch.builder().brCode("D").brName("잠실점").sumAmt(14000000).build());

        List<BranchResult.Branch> branchList2019 = new ArrayList<>();
        branchList2019.add(BranchResult.Branch.builder().brCode("A").brName("판교점").sumAmt(66800000).build());
        branchList2019.add(BranchResult.Branch.builder().brCode("B").brName("분당점").sumAmt(45400000).build());
        branchList2019.add(BranchResult.Branch.builder().brCode("C").brName("강남점").sumAmt(19500000).build());
        branchList2019.add(BranchResult.Branch.builder().brCode("D").brName("잠실점").sumAmt(6000000).build());

        List<BranchResult.Branch> branchList2020 = new ArrayList<>();
        branchList2020.add(BranchResult.Branch.builder().brCode("E").brName("을지로점").sumAmt(1000000).build());

        expectBranchResultList.add(BranchResult.builder().year(2018).dataList(branchList2018).build());
        expectBranchResultList.add(BranchResult.builder().year(2019).dataList(branchList2019).build());
        expectBranchResultList.add(BranchResult.builder().year(2020).dataList(branchList2020).build());

        // 예상 결과와 실제 결과 비교
        Assertions.assertArrayEquals(expectBranchResultList.toArray(), branchResults.toArray());
    }

    @Test
    @Order(2)
    @DisplayName("입력 받은 지점명으로 지점의 거래 합계 조회")
    public void getBranchSumAmt() {
        // when
        Mockito.when(transactionHistoryProcess.getHistoryAll()).thenReturn(histories);

        branches.forEach(branch -> {
            System.out.println("지점명 : " + branch.getBranchName());
            int sumAmt = 0;

            for(TransactionHistoryEntity history : histories) {
                AccountEntity accountInfo = accounts.stream().filter(account -> account.getAccountNo().equals(history.getAccountNo())).collect(Collectors.toList()).get(0);

                Mockito.when(accountProcess.getAccount(history.getAccountNo())).thenReturn(accountInfo);
                if(accountInfo.getBranchCode().equals(branch.getBranchCode())) {
                    sumAmt += history.getAmount();
                }
            }

            Mockito.when(branchProcess.getBranchBybranchName(branch.getBranchName())).thenReturn(branch);

            // when 실행
            BranchResult.Branch branchResult = branchService.getBranchSumAmt(branch.getBranchName());

            // 예상 결과
            BranchResult.Branch expectBranchInfo = BranchResult.Branch.builder()
                    .brName(branch.getBranchName())
                    .brCode(branch.getBranchCode())
                    .sumAmt(sumAmt)
                    .build();

            // 예상 결과와 실제 결과 비교
            Assertions.assertEquals(expectBranchInfo, branchResult);
        });
    }
}
