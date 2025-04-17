package com.swings.matchgroup.repository;

import com.swings.matchgroup.dto.MatchGroupNearbyProjection;
import com.swings.matchgroup.entity.MatchGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchGroupRepository extends JpaRepository<MatchGroupEntity, Long> {

    // 모든 매치그룹 조회
    @Query("SELECT m FROM MatchGroupEntity m JOIN FETCH m.host WHERE m.deleted = false")
    List<MatchGroupEntity> findAllWithHost();

    // 매치그룹 ID로 조회
    @Query("SELECT g FROM MatchGroupEntity g JOIN FETCH g.host WHERE g.matchGroupId = :groupId AND g.deleted = false")
    Optional<MatchGroupEntity> findById(@Param("groupId") Long groupId);

    // 호스트 ID로 조회
    @Query("SELECT g FROM MatchGroupEntity g JOIN FETCH g.host WHERE g.host.userId = :hostId AND g.deleted = false")
    List<MatchGroupEntity> findByHostUserId(@Param("hostId") Long hostId);

    // 실제 거리 찾기(지구 반경 고려)
    @Query(value = "SELECT m.match_group_id AS matchGroupId, m.group_name AS groupName, m.location, " +
            "m.latitude, m.longitude, m.schedule, m.play_style, m.gender_ratio, m.skill_level, " +
            "m.age_range, m.description, m.max_participants, m.match_type, " +
            "u.user_id AS hostId, u.username AS hostUsername " +
            "FROM match_group m " +
            "JOIN users u ON m.host_id = u.user_id " +
            "WHERE m.latitude IS NOT NULL AND m.longitude IS NOT NULL AND " +
            "(6371 * acos(cos(radians(:lat)) * cos(radians(m.latitude)) * " +
            "cos(radians(m.longitude) - radians(:lon)) + sin(radians(:lat)) * sin(radians(m.latitude)))) < :radius",
            nativeQuery = true)
    List<MatchGroupNearbyProjection> findNearbyGroupsProjected(
            @Param("lat") double lat,
            @Param("lon") double lon,
            @Param("radius") double radiusInKm
    );

}