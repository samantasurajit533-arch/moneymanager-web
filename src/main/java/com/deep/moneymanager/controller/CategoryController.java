package com.deep.moneymanager.controller;


import com.deep.moneymanager.dto.CategoryDTO;
import com.deep.moneymanager.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequiredArgsConstructor
@RequestMapping("/categories")
public class CategoryController {

    private  final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDTO> saveCategory(@RequestBody CategoryDTO categoryDTO){
        CategoryDTO savedCategory=categoryService.saveCategory(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    @GetMapping("")
    public  ResponseEntity<List<CategoryDTO>> grtCategories(){
        List<CategoryDTO> categories= categoryService.getCategoriesForCurrentUser();
        return  ResponseEntity.ok(categories);
    }
@GetMapping("/{type}")
    public  ResponseEntity<List<CategoryDTO>>getCategoriesByTypeForCurrentUser(@PathVariable String type){
        List<CategoryDTO>list=categoryService.getCategoriesByTypeForCurrentUser(type);
        return ResponseEntity.ok(list);

    }

    @PutMapping("/{categoryId}")
    public  ResponseEntity<CategoryDTO>updateCategory(@PathVariable Long categoryId
                                                      ,@RequestBody CategoryDTO dto){
        categoryService.updateCategory(categoryId,dto);
        CategoryDTO updatedCategory=categoryService.updateCategory(categoryId,dto);
        return  ResponseEntity.ok(updatedCategory);

    }
}
