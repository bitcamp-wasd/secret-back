package com.example.user.repository;

import com.example.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByNickname(String nickname);

    boolean existsByEmail(String email);

    UserEntity findByEmail(String email);

    @Query(value = "SELECT DISTINCT * FROM user_info u WHERE u.user_id IN :userId", nativeQuery = true)
    List<UserEntity> findByUserIdIn(List<Long> userId);
}
