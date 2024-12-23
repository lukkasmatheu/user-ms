package com.vm.user.repository;

import com.vm.user.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserEntity,String> {

    UserEntity findByName(String name);

    @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
    Page<UserEntity> findByNameLike(String namePattern, Pageable pageable);

}
