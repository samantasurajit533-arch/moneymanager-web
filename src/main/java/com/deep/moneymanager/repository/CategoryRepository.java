package com.deep.moneymanager.repository;

import com.deep.moneymanager.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    // select * from tbl_categories where profile_id=?
    List<CategoryEntity> findByProfile_Id(Long profileId);

    // select * from tbl_categories where id=? and profile_id=?
    Optional<CategoryEntity> findByIdAndProfile_Id(Long id, Long profileId);

    // select * from tbl_categories where type=? and profile_id=?
    List<CategoryEntity> findByTypeAndProfile_Id(String type, Long profileId);

    Boolean existsByNameAndProfile_Id(String name, Long profileId);
}