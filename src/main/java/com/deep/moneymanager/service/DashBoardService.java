package com.deep.moneymanager.service;

import com.deep.moneymanager.dto.ExpenceDTO;
import com.deep.moneymanager.dto.IncomeDTO;
import com.deep.moneymanager.dto.RecentTransactionDTO;
import com.deep.moneymanager.entity.ProfileEntity;
import com.deep.moneymanager.service.ExpenseService;
import com.deep.moneymanager.service.IncomeService;
import com.deep.moneymanager.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class DashBoardService {

    private final ProfileService profileService;
    private final ExpenseService expenseService;
    private final IncomeService incomeService;

    public Map<String, Object> getDashboardData() {
        ProfileEntity profile = profileService.getCurrentProfile();

        List<IncomeDTO> latestIncome = incomeService.getTop5IncomeForCurrentUser();
        List<ExpenceDTO> latestExpenses = expenseService.getTop5ExpensesForCurrentUser();

        // Merge and sort transactions by date, then by creation time
        List<RecentTransactionDTO> recentTransactions = Stream.concat(
                        latestIncome.stream().map(income -> RecentTransactionDTO.builder()
                                .id(income.getId())
                                .profileId(profile.getId())
                                .icon(income.getIcon())
                                .name(income.getName()) // Added name
                                .amount(income.getAmount())
                                .date(income.getDate()) // Added date for sorting
                                .createdAt(income.getCreatedAt())
                                .updatedAt(income.getUpdateAt())
                                .type("income")
                                .build()),
                        latestExpenses.stream().map(expense -> RecentTransactionDTO.builder()
                                .id(expense.getId())
                                .profileId(profile.getId()) // Fixed profileId
                                .icon(expense.getIcon())
                                .name(expense.getName())
                                .amount(expense.getAmount())
                                .date(expense.getDate())
                                .createdAt(expense.getCreatedAt())
                                .type("expense")
                                .build())
                )
                .sorted((a, b) -> {
                    int cmp = b.getDate().compareTo(a.getDate());
                    if (cmp == 0 && a.getCreatedAt() != null && b.getCreatedAt() != null) {
                        return b.getCreatedAt().compareTo(a.getCreatedAt());
                    }
                    return cmp;
                })
                .limit(5) // Usually, dashboards only want the top 5 total
                .collect(Collectors.toList());

        Map<String, Object> returnValue = new LinkedHashMap<>();
        returnValue.put("totalBalance", incomeService.getTotalIncomeForCurrentUser()
                .subtract(expenseService.getTotalExpensesForCurrentUser()));
        returnValue.put("totalExpense", expenseService.getTotalExpensesForCurrentUser());
        returnValue.put("recent5expenses", latestExpenses);
        returnValue.put("recent5income", latestIncome);
        returnValue.put("recentTransactions", recentTransactions);

        return returnValue;
    }
}
