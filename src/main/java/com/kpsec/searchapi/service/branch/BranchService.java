package com.kpsec.searchapi.service.branch;

import com.kpsec.searchapi.model.entity.AccountEntity;
import com.kpsec.searchapi.model.entity.BranchEntity;
import com.kpsec.searchapi.model.entity.TransactionHistoryEntity;
import com.kpsec.searchapi.model.result.BranchResult;
import com.kpsec.searchapi.process.account.AccountProcess;
import com.kpsec.searchapi.process.branch.BranchProcess;
import com.kpsec.searchapi.process.history.TransactionHistoryProcess;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 관리점 관련 서비스 클래스
 * 
 * Create By na kyutae 2021-10-30
 */
@Service
@RequiredArgsConstructor
public class BranchService {

    private final AccountProcess accountProcess;
    private final BranchProcess branchProcess;
    private final TransactionHistoryProcess transactionHistoryProcess;

    /**
     * 내림차순으로 연도 정렬
     */
    private final Comparator<BranchResult> yearDescCompare = new Comparator<>() {
        @Override
        public int compare(BranchResult o1, BranchResult o2) {
            return Integer.compare(o1.getYear(), o2.getYear());
        }
    };

    /**
     * 내림차순으로 거래 금액 정렬
     */
    private final Comparator<BranchResult.Branch> sumAmtDescCompare = new Comparator<>() {
        @Override
        public int compare(BranchResult.Branch o1, BranchResult.Branch o2) {
            return Integer.compare(o2.getSumAmt(), o1.getSumAmt());
        }
    };

    /**
     * 연도별, 관리점 별 거래 금액의 순서를 조회하는 메서드
     * 
     * @return List<BranchResult> 지점 리스트
     */
    public List<BranchResult> getBranchOfTopForYear() {
        List<BranchResult> branchResults = new ArrayList<>();

        List<BranchEntity> branchsInfo = branchProcess.getBranchAll();
        List<String> years = transactionHistoryProcess.getHistoryDistinctYear();

        years.forEach(year -> {
            List<TransactionHistoryEntity> histories = transactionHistoryProcess.getHistory(year);
            List<BranchResult.Branch> branches = new ArrayList<>();
            branchsInfo.forEach(branchInfo -> {
                int sumAmt = 0;

                for(TransactionHistoryEntity history : histories) {
                    AccountEntity account = accountProcess.getAccount(history.getAccountNo());

                    if(account.getBranchCode().equals(branchInfo.getBranchCode())) {
                        sumAmt += history.getAmount();
                    }
                }

                if(sumAmt == 0) return;

                BranchResult.Branch branch = BranchResult.Branch.builder()
                        .brName(branchInfo.getBranchName())
                        .brCode(branchInfo.getBranchCode())
                        .sumAmt(sumAmt)
                        .build();

                branches.add(branch);
            });

            BranchResult branchResult = BranchResult.builder()
                    .year(Integer.parseInt(year))
                    .dataList(branches.stream().sorted(sumAmtDescCompare).collect(Collectors.toList()))
                    .build();

            branchResults.add(branchResult);
        });

        return branchResults.stream().sorted(yearDescCompare).collect(Collectors.toList());
    }

    /**
     * 지점명을 입력 받아 해당 지점의 총 거래금액을 조회하는 메서드
     * 
     * @param branchName 지점명
     * @return BranchResult.Branch 지점의 정보 및 총 거래금액
     */
    public BranchResult.Branch getBranchSumAmt(String branchName) {
        List<TransactionHistoryEntity> histories = transactionHistoryProcess.getHistoryAll();
        BranchEntity branchInfo = branchProcess.getBranchBybranchName(branchName);

        int sumAmt = 0;

        for(TransactionHistoryEntity history : histories) {
            AccountEntity account = accountProcess.getAccount(history.getAccountNo());

            if(account.getBranchCode().equals(branchInfo.getBranchCode())) {
                sumAmt += history.getAmount();
            }
        }

        return BranchResult.Branch.builder()
                .brName(branchInfo.getBranchName())
                .brCode(branchInfo.getBranchCode())
                .sumAmt(sumAmt)
                .build();
    }
}
