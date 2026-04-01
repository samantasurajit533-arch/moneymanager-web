package com.deep.moneymanager.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

    @Data
    @Entity
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public class ExpenceDTO{

        @Id
        @GeneratedValue()
        private Long id;
        private String name;
        private String icon;
        private  String categoryName;
        private  Long categoryId;
        private LocalDate date;
        private LocalDateTime createdAt;
        private  LocalDateTime updateAt;
        private BigDecimal amount;

    }
