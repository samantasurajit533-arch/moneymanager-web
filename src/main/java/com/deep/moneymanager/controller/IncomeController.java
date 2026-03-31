package com.deep.moneymanager.controller;


import com.deep.moneymanager.dto.ExpenceDTO;
import com.deep.moneymanager.dto.IncomeDTO;
import com.deep.moneymanager.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/incomes")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping()
    public ResponseEntity<IncomeDTO> addIncome(@RequestBody IncomeDTO incomeDTO){
        IncomeDTO saved=incomeService.addIncome(incomeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    @GetMapping()
    public ResponseEntity<IncomeDTO> getExpenses(){
        List<IncomeDTO> incomeDTOS=incomeService.getCurrentMonthIncomesForCurrentUser();
        return ResponseEntity.ok((IncomeDTO)  incomeDTOS);
    }
    @DeleteMapping("/{id}")
    public  ResponseEntity<IncomeDTO>deletedIncome(@PathVariable Long id){
        incomeService.deletedExpenses(id);
        return ResponseEntity.noContent().build();
    }

}
