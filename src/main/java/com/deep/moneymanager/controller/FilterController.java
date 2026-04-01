package com.deep.moneymanager.controller;


import com.deep.moneymanager.dto.ExpenceDTO;
import com.deep.moneymanager.dto.FilterDTO;
import com.deep.moneymanager.dto.IncomeDTO;
import com.deep.moneymanager.service.ExpenseService;
import com.deep.moneymanager.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filter")
public class FilterController {
    private final ExpenseService expenseService;
    private final IncomeService incomeService;


    @PostMapping("/filter")
    private ResponseEntity<?>filterTransaction(@RequestBody FilterDTO filter){
//preparing the data and validition
        LocalDate started=filter.getStartDate()!=null?filter.getStartDate():LocalDate.MIN;
        LocalDate enDate=filter.getEndDate()!=null?filter.getEndDate():LocalDate.now();
        String sortField= filter.getSortField()!=null? filter.getSortField():"date";
        Sort.Direction direction="desc".equalsIgnoreCase(filter.getSortOrder())?Sort.Direction.DESC:Sort.Direction.ASC;
       Sort sort=Sort.by(direction,sortField);
       if("income".equals(filter.getType())) {
           List<IncomeDTO> income = incomeService.filterIncome(started,enDate, filter.getKeyword(), sort);
           return ResponseEntity.ok(income);
       }else if("expense".equalsIgnoreCase(filter.getType())){
           List<ExpenceDTO>expenceDTOS=expenseService.filterExpenses(started,enDate,filter.getKeyword(),sort);
           return ResponseEntity.ok(expenceDTOS);
       }else{
        return ResponseEntity.badRequest().body("Invalid type.Must be 'income' or 'expense'");
    }


    }
}
