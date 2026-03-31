package com.deep.moneymanager.controller;


import com.deep.moneymanager.dto.CategoryDTO;
import com.deep.moneymanager.dto.ExpenceDTO;
import com.deep.moneymanager.service.ExpenseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.sql.ClientInfoStatus;
import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenceController {

    private ExpenseService expenseService;

    @PostMapping()
    public ResponseEntity<ExpenceDTO> addExpence(@RequestBody ExpenceDTO expenceDTO){
        ExpenceDTO saved=expenseService.addExpense(expenceDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    @GetMapping()
public ResponseEntity<ExpenceDTO> getExpenses(){
        List<ExpenceDTO>expenceDTOS=expenseService.getCurrentMonthByExpensesCurrentUser();
        return ResponseEntity.ok((ExpenceDTO) expenceDTOS);
    }
    @DeleteMapping("/{id}")
    public  ResponseEntity<ExpenceDTO>deletedExpenses(@PathVariable Long id){
        expenseService.deletedExpenses(id);
        return ResponseEntity.noContent().build();
    }





}
