package com.deep.moneymanager.repository;

import com.deep.moneymanager.entity.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfileEntity,Long> {


    //selects * from tbl_profiles where email=?
    Optional<ProfileEntity>findByEmail(String email);


    //selects * from tbl_profiles where activationToken=?
    Optional<ProfileEntity>findByActivationToken(String activationToken);

}
