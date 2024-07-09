package com.example.video.repository;

import com.example.video.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    @Query("SELECT v FROM Video v JOIN FETCH v.post JOIN FETCH v.post.category LEFT JOIN FETCH v.sheetMusicList WHERE v.id=:id")
    public Optional<Video> findByIdFetch(Long id);
}
