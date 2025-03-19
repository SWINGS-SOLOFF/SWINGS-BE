package com.swings.user.repository;
import com.swings.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    //username기반으로 사용자 조회
    @Query("SELECT u FROM UserEntity u WHERE u.username = :username")
    Optional<UserEntity> findByUsername(@Param("username") String username);


}
