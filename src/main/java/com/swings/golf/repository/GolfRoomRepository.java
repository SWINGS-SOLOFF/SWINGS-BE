package com.swings.golf.repository;

import com.swings.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GolfRoomRepository extends JpaRepository<UserEntity, Long> {
}