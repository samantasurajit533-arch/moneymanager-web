package com.deep.moneymanager.service;

import com.deep.moneymanager.dto.ExpenceDTO;
import com.deep.moneymanager.entity.CategoryEntity;
import com.deep.moneymanager.entity.ExpenseEntity;
import com.deep.moneymanager.entity.ProfileEntity;
import com.deep.moneymanager.repository.CategoryRepository;
import com.deep.moneymanager.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final CategoryRepository categoryRepository;
    private  ProfileService profileService;
    private final ExpenseRepository expenseRepository;


    public ExpenceDTO addExpense(ExpenceDTO dto){
        ProfileEntity profile= profileService.getCurrentProfile();
         CategoryEntity  category=categoryRepository.findById(dto.getCategoryId())
                 .orElseThrow(()->new RuntimeException("Category not found"));
         ExpenseEntity newExpense=toEntity(dto,profile,category);
         newExpense=expenseRepository.save(newExpense);
         return  toDTO(newExpense);

    }
    //reterives

public List<ExpenceDTO> getCurrentMonthByExpensesCurrentUser(){
        ProfileEntity profile=profileService.getCurrentProfile();
        LocalDate now=LocalDate.now();
       LocalDate startDate= now.withDayOfMonth(1);
       LocalDate endDate=now.withDayOfMonth(now.lengthOfMonth());
     List<ExpenseEntity> list= expenseRepository.findByProfileIdAndDateBetween(profile.getId(), startDate,endDate);
     return list.stream().map(this::toDTO).toList();
}

//deleted
    public void deletedExpenses(Long expenseId){
        ProfileEntity profile= profileService.getCurrentProfile();
        ExpenseEntity entity=expenseRepository.findById(expenseId)
                .orElseThrow(()->new RuntimeException("Expenses not found"));
        if(!entity.getId().equals(profile.getId())){
            throw new RuntimeException("unauthorized to deleted this expense");
        }
        expenseRepository.delete(entity);
    }
    //get latest 5 expenses for current user

    public List<ExpenceDTO>getTop5ExpensesForCurrentUser(){
        ProfileEntity profile=profileService.getCurrentProfile();
        List<ExpenseEntity>list=expenseRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return list.stream().map(this::toDTO).toList();
    }
    //total expenses for current user
    public BigDecimal getTotalExpensesForCurrentUser(){
        ProfileEntity profile=profileService.getCurrentProfile();
        BigDecimal total=expenseRepository.findTotalExpenseByProfileId(profile.getId());
        return total!=null?total:BigDecimal.ZERO;
    }


    //filter expenses
    public List<ExpenceDTO> filterExpenses(LocalDate startDate, LocalDate endDate, String keyword, Sort sort) {
        ProfileEntity profile = profileService.getCurrentProfile();

        // Pass the keyword and sort parameters through
        List<ExpenseEntity> list = expenseRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
                profile.getId(),
                startDate,
                endDate,
                keyword,
                sort
        );

        return list.stream()
                .map(this::toDTO)
                .toList();
    }

    //Notification
    public  List<ExpenceDTO>getExpensesForUserOnDate(Long profileId, LocalDate date){
        List<ExpenseEntity>list=expenseRepository.findByProfileIdAndDate(profileId,date);
         return list.stream().map(this::toDTO).toList();

    }






    //helper method
    private ExpenseEntity toEntity(ExpenceDTO dto, ProfileEntity profile, CategoryEntity category) {
        return ExpenseEntity.builder()
                .name(dto.getName())
                .icon(dto.getIcon())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .profile(profile)
                .category(category)
                .build();
    }

    private ExpenceDTO toDTO(ExpenseEntity entity) {

        return ExpenceDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .icon(entity.getIcon())
                .categoryId(entity.getCategory() != null ? entity.getCategory().getId() : null)
                .categoryName(entity.getCategory() != null ? entity.getCategory().getName() : "N/A")
                .amount(entity.getAmount())
                .date(entity.getDate())
                .createdAt(entity.getCreateAt())
                .updateAt(entity.getUpdatedAt())
                .build();
    }
}
