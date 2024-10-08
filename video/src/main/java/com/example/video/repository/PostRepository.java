package com.example.video.repository;

import com.example.video.entity.Category;
import com.example.video.entity.Post;
import com.example.video.entity.VideoLikeList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p FROM Post p JOIN FETCH p.video WHERE p.userId=:userId")
    Optional<Slice<Post>> findByUserId(Long userId, Pageable page);

    @Query("SELECT p FROM Post p JOIN FETCH p.category JOIN FETCH p.video")
    Optional<Slice<Post>> findAllFetch(Pageable page);

    @Query("SELECT p FROM Post p JOIN FETCH p.category JOIN FETCH p.video WHERE p.category.category IN (:categories) ")
    Optional<Slice<Post>> findAllFetch(List<String> categories, Pageable page);

    @Query("SELECT p FROM Post p JOIN FETCH p.category JOIN FETCH p.video WHERE p.title LIKE CONCAT('%', :title ,'%') ")
    Optional<Slice<Post>> findByTitle(String title, Pageable page);

    @Query("SELECT p FROM Post p JOIN FETCH p.category JOIN FETCH p.video WHERE p.title LIKE CONCAT('%', :title ,'%') AND p.category.category IN (:categories)")
    Optional<Slice<Post>> findByTitle(String title, List<String> categories, Pageable page);

    @Query("SELECT p FROM Post p JOIN FETCH p.video WHERE p.postId = :postId")
    Optional<Post> findByIdFetchVideo(Long postId);
}
