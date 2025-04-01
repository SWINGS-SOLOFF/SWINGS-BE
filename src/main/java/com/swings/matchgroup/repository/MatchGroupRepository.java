package com.swings.matchgroup.repository;

import com.swings.matchgroup.entity.MatchGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchGroupRepository extends JpaRepository<MatchGroupEntity, Long> {}
