package com.kpsec.searchapi.infra.init;

import com.kpsec.searchapi.model.entity.AccountEntity;
import com.kpsec.searchapi.model.entity.BranchEntity;
import com.kpsec.searchapi.model.entity.TransactionHistoryEntity;
import com.kpsec.searchapi.repository.AccountRepository;
import com.kpsec.searchapi.repository.BranchRepository;
import com.kpsec.searchapi.repository.TransactionHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 어플리케이션이 시작 되면 resource/static/data 폴더에 들어있는
 * 거래내역.csv, 계좌정보.csv, 관리점정보.csv 파일을 읽어 메모리 DB에 저장하는 클래스
 *
 * Create by na kyutae 2021-10-29.
 */
@Component
@RequiredArgsConstructor
public class InitData {

    private final AccountRepository accountRepository; // 계좌 정보 Repository
    private final TransactionHistoryRepository transactionHistoryRepository; // 거래 내역 Repository
    private final BranchRepository managementPointRepository; // 관리점 Repository

    public void init() throws IOException {
        this.initAccount();
        this.initHistory();
        this.initManagementPoint();
    }

    /**
     * 계좌 정보를 읽어와 메모리 DB에 저장하는 메서드
     *
     * @throws IOException
     */
    @PostConstruct
    private void initAccount() throws IOException {
        if (accountRepository.count() == 0) {
            Resource resource = new ClassPathResource("static/data/계좌정보.csv");
            byte[] resourceData = FileCopyUtils.copyToByteArray(resource.getInputStream());
            String data = new String(resourceData, StandardCharsets.UTF_8);
            String[] lines = data.split("\r\n");

            List<AccountEntity> accounts = Arrays.stream(lines).skip(1).map(line -> {
                        String[] split = line.split(",");
                        return AccountEntity.builder()
                                .accountNo(split[0])
                                .accountName(split[1])
                                .branchCode(split[2])
                                .build();
                    }).collect(Collectors.toList());

            accountRepository.saveAll(accounts);
        }
    }

    /**
     * 거래 내역을 읽어와 메모리 DB에 저장하는 메서드
     *
     * @throws IOException
     */
    @PostConstruct
    private void initHistory() throws IOException {
        if (transactionHistoryRepository.count() == 0) {
            Resource resource = new ClassPathResource("static/data/거래내역.csv");
            byte[] resourceData = FileCopyUtils.copyToByteArray(resource.getInputStream());
            String data = new String(resourceData, StandardCharsets.UTF_8);
            String[] lines = data.split("\r\n");

            List<TransactionHistoryEntity> histories = Arrays.stream(lines).skip(1).map(line -> {
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

            transactionHistoryRepository.saveAll(histories);
        }
    }

    /**
     * 관리점 정보를 읽어와 메모리 DB에 저장하는 메서드
     *
     * @throws IOException
     */
    @PostConstruct
    private void initManagementPoint() throws IOException {
        if (managementPointRepository.count() == 0) {
            Resource resource = new ClassPathResource("static/data/관리점정보.csv");
            byte[] resourceData = FileCopyUtils.copyToByteArray(resource.getInputStream());
            String data = new String(resourceData, StandardCharsets.UTF_8);
            String[] lines = data.split("\r\n");

            List<BranchEntity> managementPoints = Arrays.stream(lines).skip(1).map(line -> {
                        String[] split = line.split(",");
                        return BranchEntity.builder()
                                .branchCode(split[0])
                                .branchName(split[1])
                                .build();
                    }).collect(Collectors.toList());

            managementPointRepository.saveAll(managementPoints);
        }
    }

}
