package com.example.video.repository;

import com.example.video.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video, Long> {

    @Query("SELECT DISTINCT v FROM Video v JOIN FETCH v.post WHERE v.id=:id")
    public Optional<Video> findByIdFetch(Long id);
}
