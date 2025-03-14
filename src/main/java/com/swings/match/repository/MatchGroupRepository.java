package com.swings.match.repository;

import com.swings.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchGroupRepository extends JpaRepository<UserEntity, Long> {
}