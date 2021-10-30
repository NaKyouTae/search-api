package com.kpsec.searchapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpsec.searchapi.common.exception.NotFoundException;
import com.kpsec.searchapi.controller.branch.BranchController;
import com.kpsec.searchapi.model.entity.AccountEntity;
import com.kpsec.searchapi.model.entity.BranchEntity;
import com.kpsec.searchapi.model.entity.TransactionHistoryEntity;
import com.kpsec.searchapi.model.result.BranchResult;
import com.kpsec.searchapi.process.account.AccountProcess;
import com.kpsec.searchapi.process.branch.BranchProcess;
import com.kpsec.searchapi.process.history.TransactionHistoryProcess;
import com.kpsec.searchapi.service.branch.BranchService;
import com.kpsec.searchapi.service.branch.BranchValidationService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Create By na kyutae 2021-10-31
 */
@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = BranchController.class)
public class BranchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BranchService branchService;
    @MockBean
    private BranchValidationService branchValidationService;

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
    @DisplayName("연도별, 관리점 별 거래 금액의 순서를 조회")
    public void getBranchOfTopForYear() throws Exception {
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

        Mockito.when(branchService.getBranchOfTopForYear()).thenReturn(expectBranchResultList);

        MvcResult mvcResult = mockMvc.perform(get("/branches/amounts")
                        .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        ObjectMapper om = new ObjectMapper();
        Map<String, String> map = om.readValue(mvcResult.getResponse().getContentAsString(), Map.class);

        Assertions.assertEquals(map.get("successed"), "true");
    }
    
    @Test
    @Order(2)
    @DisplayName("지점명을 입력 받아 해당 지점의 총 거래금액을 조회")
    public void getBranchSumAmt() {
        branches.forEach(branch -> {
            if(branch.getBranchName().equals("분당점")) return;

            System.out.println("지점명 : " + branch.getBranchName());
            int sumAmt = 0;

            for(TransactionHistoryEntity history : histories) {
                AccountEntity accountInfo = accounts.stream().filter(account -> account.getAccountNo().equals(history.getAccountNo())).collect(Collectors.toList()).get(0);

                if(accountInfo.getBranchCode().equals(branch.getBranchCode())) {
                    sumAmt += history.getAmount();
                }
            }

            // 예상 결과
            BranchResult.Branch expectBranchInfo = BranchResult.Branch.builder()
                    .brName(branch.getBranchName())
                    .brCode(branch.getBranchCode())
                    .sumAmt(sumAmt)
                    .build();

            Mockito.when(branchService.getBranchSumAmt(branch.getBranchName())).thenReturn(expectBranchInfo);

            try {
                MvcResult mvcResult = mockMvc.perform(get("/branches/names?branchName=" + branch.getBranchName())
                        .contentType("application/json;charset=UTF-8"))
                        .andExpect(status().isOk())
                        .andDo(print())
                        .andReturn();

                ObjectMapper om = new ObjectMapper();
                Map<String, String> map = om.readValue(mvcResult.getResponse().getContentAsString(), Map.class);

                Assertions.assertEquals(map.get("successed"), "true");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    @Order(2)
    @DisplayName("분당점의 총 거래금액 조회시 404에러")
    public void getBranchSumAmt_thenReturn404() {
        String brName = "분당점";
        try {

            Mockito.when(branchValidationService.validationBranchName(brName)).thenThrow(new NotFoundException("br code not found error"));

            MvcResult mvcResult = mockMvc.perform(get("/branches/names?branchName=" + brName)
                    .contentType("application/json;charset=UTF-8"))
                    .andExpect(status().is4xxClientError())
                    .andDo(print())
                    .andReturn();

            System.out.println("error mgs : " + mvcResult.getResponse().getErrorMessage());
            Assertions.assertEquals(mvcResult.getResponse().getStatus(), 404);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
