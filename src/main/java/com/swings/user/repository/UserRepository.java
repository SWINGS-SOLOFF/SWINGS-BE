package com.swings.user.repository;

import com.swings.user.entity.UserEntity;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    //id로 사용자 찾기
    @Query("select u from UserEntity u where u.id = :id")
    Optional<UserEntity> findById(String id);

}