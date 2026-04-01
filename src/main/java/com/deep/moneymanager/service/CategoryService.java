package com.deep.moneymanager.service;


import com.deep.moneymanager.dto.CategoryDTO;
import com.deep.moneymanager.entity.CategoryEntity;
import com.deep.moneymanager.entity.ProfileEntity;
import com.deep.moneymanager.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private  final  ProfileService profileService;
    private final CategoryRepository categoryRepository;

    //save catagory
    public CategoryDTO saveCategory(CategoryDTO categoryDTO){
            ProfileEntity profile=profileService.getCurrentProfile();
        if(categoryRepository.existsByNameAndProfile_Id(categoryDTO.getName(), profile.getId() )){
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Category with this name already exist");
        }
        CategoryEntity newCategory=toEntity(categoryDTO,profile);
        newCategory=categoryRepository.save(newCategory);
        return  toDTO(newCategory);
    }

    //get categories for current user

    public List<CategoryDTO>getCategoriesForCurrentUser(){
        ProfileEntity profile=profileService.getCurrentProfile();
        List<CategoryEntity>categories=categoryRepository.findByProfile_Id(Long.valueOf(String.valueOf(profile.getId())));
        return  categories.stream().map(this::toDTO).toList();
    }

    //get cataroires  by type for  curent user
    public List<CategoryDTO>getCategoriesByTypeForCurrentUser(String type){
        ProfileEntity profile=profileService.getCurrentProfile();
        List<CategoryEntity>entities=categoryRepository.findByTypeAndProfile_Id(type, profile.getId());
         return entities.stream().map(this::toDTO).toList();
    }

    //update Catagories DTO

    public  CategoryDTO updateCategory(Long categoryId,CategoryDTO dto){
        ProfileEntity profile=profileService.getCurrentProfile();
          CategoryEntity entity=categoryRepository.findByIdAndProfile_Id(categoryId, profile.getId())
                .orElseThrow(()->new RuntimeException("Category did not found"));

          entity.setName(dto.getName());
          entity.setIcon(dto.getIcon());
          entity=categoryRepository.save(entity);
          return  toDTO(entity);

    }


    //helper

    private CategoryEntity toEntity(CategoryDTO categoryDTO, ProfileEntity profile){
        return CategoryEntity.builder()
                .name(categoryDTO.getName())
                .icon(categoryDTO.getIcon())
                .profile(profile)
                .type(categoryDTO.getType())
                .build();
    }
    private  CategoryDTO toDTO(CategoryEntity category){
        return CategoryDTO.builder()
                .id(category.getId())
                .profileId(category.getProfile()!=null?category.getProfile().getId():null)
                .name(category.getName())
                .icon(category.getIcon())
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .type(category.getType())
                .build();
    }
}
