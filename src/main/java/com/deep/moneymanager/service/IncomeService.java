package com.deep.moneymanager.service;

import com.deep.moneymanager.dto.ExpenceDTO;
import com.deep.moneymanager.dto.IncomeDTO;
import com.deep.moneymanager.entity.CategoryEntity;
import com.deep.moneymanager.entity.ExpenseEntity;
import com.deep.moneymanager.entity.IncomeEntity;
import com.deep.moneymanager.entity.ProfileEntity;
import com.deep.moneymanager.repository.CategoryRepository;
import com.deep.moneymanager.repository.IncomeRepository;
import com.deep.moneymanager.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeService {

    private final ProfileService profileService;
    private final CategoryRepository categoryRepository;
    private final IncomeRepository incomeRepository;

    public IncomeDTO addIncome(IncomeDTO dto) {
        ProfileEntity profile = profileService.getCurrentProfile();
        CategoryEntity category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        IncomeEntity newIncome = toEntity(dto, profile, category);
        newIncome = incomeRepository.save(newIncome);
        return toDTO(newIncome);
    }


    public List<IncomeDTO> getCurrentMonthIncomesForCurrentUser(){
        ProfileEntity income=profileService.getCurrentProfile();
        LocalDate now=LocalDate.now();
        LocalDate startDate= now.withDayOfMonth(1);
        LocalDate endDate=now.withDayOfMonth(now.lengthOfMonth());
        List<IncomeEntity> list=incomeRepository.findByProfileIdAndDateBetween(income.getId(), startDate,endDate);
        return list.stream().map(this::toDTO).toList();
    }
    public void deletedExpenses(Long incomeId){
        ProfileEntity profile= profileService.getCurrentProfile();
        IncomeEntity entity=incomeRepository.findById(incomeId)
                .orElseThrow(()->new RuntimeException("Expenses not found"));
        if(!entity.getId().equals(profile.getId())){
            throw new RuntimeException("unauthorized to deleted this expense");
        }
        incomeRepository.delete(entity);
    }
    //get latest 5 income for current user

    public List<IncomeDTO>getTop5IncomeForCurrentUser(){
        ProfileEntity profile=profileService.getCurrentProfile();
        List<IncomeEntity>list=incomeRepository.findTop5ByProfileIdOrderByDateDesc(profile.getId());
        return list.stream().map(this::toDTO).toList();
    }
    //total expenses for current user
    public BigDecimal getTotalIncomeForCurrentUser(){
        ProfileEntity profile=profileService.getCurrentProfile();
        BigDecimal total=incomeRepository.findTotalExpenseByProfileId(profile.getId());
        return total!=null?total:BigDecimal.ZERO;
    }

    //
    public List<IncomeDTO> filterIncome(LocalDate startDate, LocalDate endDate, String keyword, Sort sort) {
        ProfileEntity profile = profileService.getCurrentProfile();

        // Pass the keyword and sort parameters through
        List<IncomeEntity> list = incomeRepository.findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
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




    private IncomeEntity toEntity(IncomeDTO dto, ProfileEntity profile, CategoryEntity category) {
        return IncomeEntity.builder()
                .name(dto.getName())
                .icon(dto.getIcon())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .profile(profile)
                .category(category)
                .build();
    }

    private IncomeDTO toDTO(IncomeEntity entity) {
        return IncomeDTO.builder()
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
