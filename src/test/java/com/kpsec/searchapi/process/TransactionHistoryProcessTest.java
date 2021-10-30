package com.kpsec.searchapi.process;

import com.kpsec.searchapi.model.entity.TransactionHistoryEntity;
import com.kpsec.searchapi.process.history.TransactionHistoryProcess;
import com.kpsec.searchapi.repository.TransactionHistoryRepository;
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
 * Create By na kyutae 2021-10-31
 */
@ExtendWith(MockitoExtension.class)
public class TransactionHistoryProcessTest {

    @InjectMocks
    private TransactionHistoryProcess transactionHistoryProcess;

    @Mock
    private TransactionHistoryRepository transactionHistoryRepository;
    @Mock
    private List<TransactionHistoryEntity> histories;

    @BeforeEach
    public void getHistoryBefore() throws IOException {
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
    @DisplayName("거래 내역에서 연도를 중복제거 하여 조회")
    public void getHistoryDistinctYear() {
        Mockito.when(transactionHistoryProcess.getHistoryAll()).thenReturn(histories);

        List<String> years = transactionHistoryProcess.getHistoryDistinctYear();

        List<String> expectYears = new ArrayList<>();

        expectYears.add("2018");
        expectYears.add("2019");
        expectYears.add("2020");

        Assertions.assertArrayEquals(expectYears.toArray(), years.toArray());
    }
}
