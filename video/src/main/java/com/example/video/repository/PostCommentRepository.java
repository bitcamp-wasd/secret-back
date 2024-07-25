package com.example.video.repository;

import com.example.video.dto.auth.UserAuth;
import com.example.video.dto.comment.request.DeleteCommentRequestDto;
import com.example.video.dto.comment.response.MyCommentResponseDto;
import com.example.video.entity.PostComment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface PostCommentRepository extends JpaRepository<PostComment, Long> {

    @Query("SELECT c FROM PostComment c WHERE c.post.postId = :postId ")
    Slice<PostComment> findByPostId(Long postId, Pageable page);

    @Modifying
    @Transactional
    @Query("DELETE FROM PostComment c WHERE c.userId = :userId AND c.commentId IN (:deleteCommentRequestDto)")
    int deleteAllById(Long userId, List<Long> deleteCommentRequestDto);

    @Query("SELECT c FROM PostComment c JOIN FETCH c.post p WHERE c.userId = :userId")
    Page<PostComment> findByIdFetchPost(Long userId, Pageable page);
}
