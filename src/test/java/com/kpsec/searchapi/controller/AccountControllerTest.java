package com.kpsec.searchapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kpsec.searchapi.controller.account.AccountController;
import com.kpsec.searchapi.model.result.AccountResult;
import com.kpsec.searchapi.model.result.NoAccountResult;
import com.kpsec.searchapi.service.account.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Create By na kyutae 2021-10-31
 */
@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = AccountController.class)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Test
    @Order(1)
    @DisplayName("연도별 거래금액이 가장 큰 계좌 조회")
    public void getAccountOfTopForYear() throws Exception {
        List<String> years = new ArrayList<>();
        years.add("2019");
        years.add("2018");

        // 예상 결과
        List<AccountResult> accountResults = new ArrayList<>();

        accountResults.add(AccountResult.builder().year(2018).name("테드").acctNo("11111114").sumAmt(28992000).build());
        accountResults.add(AccountResult.builder().year(2019).name("에이스").acctNo("11111112").sumAmt(40998400).build());

        Mockito.when(accountService.getAccountOfTopForYear(years)).thenReturn(accountResults);

        MvcResult mvcRes = mockMvc.perform(get("/accounts/histories-years-top?years=2019,2018")
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        ObjectMapper om = new ObjectMapper();

        Map<String, String> map = om.readValue(mvcRes.getResponse().getContentAsString(), Map.class);

        Assertions.assertEquals(map.get("successed"), "true");
    }
    
    @Test
    @Order(2)
    @DisplayName("연도별 거래내역이 없는 계좌 조회")
    public void getNoHistoryAccountForYear() throws Exception {
        List<String> years = new ArrayList<>();
        years.add("2019");
        years.add("2018");

        // 예상 결과
        List<NoAccountResult> accountResults = new ArrayList<>();

        accountResults.add(NoAccountResult.builder().year(2018).name("사라").acctNo("11111115").build());
        accountResults.add(NoAccountResult.builder().year(2018).name("제임스").acctNo("11111118").build());
        accountResults.add(NoAccountResult.builder().year(2018).name("에이스").acctNo("11111121").build());
        accountResults.add(NoAccountResult.builder().year(2019).name("테드").acctNo("11111114").build());
        accountResults.add(NoAccountResult.builder().year(2019).name("제임스").acctNo("11111118").build());
        accountResults.add(NoAccountResult.builder().year(2019).name("에이스").acctNo("11111121").build());

        Mockito.when(accountService.getNoHistoryAccountForYear(years)).thenReturn(accountResults);

        MvcResult mvcRes = mockMvc.perform(get("/accounts/histories-years-none?years=2019,2018")
                .contentType("application/json;charset=UTF-8"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        ObjectMapper om = new ObjectMapper();

        Map<String, String> map = om.readValue(mvcRes.getResponse().getContentAsString(), Map.class);

        Assertions.assertEquals(map.get("successed"), "true");
    }
}
