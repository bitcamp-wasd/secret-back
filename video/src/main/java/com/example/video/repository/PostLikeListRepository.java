package com.example.video.repository;

import com.example.video.entity.PostLikeList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeListRepository extends JpaRepository<PostLikeList, Long> {
}
