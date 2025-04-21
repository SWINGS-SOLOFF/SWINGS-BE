package com.swings.user.repository;
import com.swings.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);  // 아이디 중복 확인을 위한 메서드

    Optional<UserEntity> findByEmail(String email);  // 이메일 중복 확인을 위한 메서드

    Optional<UserEntity> findByUsernameAndEmail(String username, String email);


}
