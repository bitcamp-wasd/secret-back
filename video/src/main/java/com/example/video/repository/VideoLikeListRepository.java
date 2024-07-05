package com.example.video.repository;

import com.example.video.entity.Video;
import com.example.video.entity.VideoLikeList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoLikeListRepository extends JpaRepository<VideoLikeList, Long> {

    @Query("SELECT v FROM VideoLikeList v WHERE v.video =:video AND v.userId=:userId")
    Optional<VideoLikeList> findVideoAndUserId(Video video, Long userId);

    @Query("SELECT v FROM VideoLikeList v WHERE v.video.id =:videoId AND v.userId=:userId")
    Optional<VideoLikeList> existVideoIdAndUserId(Long videoId, Long userId);
}