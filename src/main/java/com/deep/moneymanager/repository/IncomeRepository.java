package com.deep.moneymanager.repository;

import com.deep.moneymanager.entity.ExpenseEntity;
import com.deep.moneymanager.entity.IncomeEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IncomeRepository  extends  JpaRepository<IncomeEntity,Long>
{

    List<IncomeEntity> findByProfileIdOrderByDateDesc(Long profileId);

    List<IncomeEntity>findTop5ByProfileIdOrderByDateDesc(Long profileId);


    @Query("SELECT SUM(i.amount) FROM IncomeEntity i where i.profile.id=:profileId")
    BigDecimal findTotalExpenseByProfileId(@Param( "profiledId")Long profileId);

    List<IncomeEntity>findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
            Long profileId,
            LocalDate startDate,
            LocalDate endDate,
            String keyword,
            Sort sort
    );
   List<IncomeEntity>findByProfileIdAndDateBetween(Long profileId,LocalDate startDate,LocalDate endDate);






}
