package com.app.persistence.repository;

import com.app.persistence.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long>{

    //Creamos un metodo personalizado que busque mediante el "username"
    Optional<UserEntity> findUserEntityByUsername(String username);
}
