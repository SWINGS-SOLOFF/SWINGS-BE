package com.swings.matchgroupchat;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchGroupChatRepository extends JpaRepository<MatchGroupChatEntity, Long> {
    List<MatchGroupChatEntity> findByMatchGroup_MatchGroupIdOrderBySentAt(Long matchGroupId);
}
