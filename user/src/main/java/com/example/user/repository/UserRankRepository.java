package com.example.user.repository;

import com.example.user.entity.UserRankEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRankRepository extends JpaRepository<UserRankEntity, Long> {

    Optional<UserRankEntity> findByMinPoint(int minPoint);
}
