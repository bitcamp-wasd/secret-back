package com.example.video.entity;

import com.example.video.dto.comment.response.CommentResponseDto;
import com.example.video.dto.comment.response.MyCommentResponseDto;
import com.example.video.dto.comment.response.UserResponseDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Table(name="post_comment")
public class PostComment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private Long commentId;

    @Column(name="create_date")
    @CreationTimestamp
    private LocalDateTime createDate;

    @Column(name="user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

    private String comment;

    public PostComment(Long userId, Post post, String comment) {
        this.userId = userId;
        this.post = post;
        this.comment = comment;
    }

    public CommentResponseDto toCommentResponseDto(UserResponseDto userResponseDto) {
        return new CommentResponseDto(
                this.commentId,
                userResponseDto.getNickname(),
                userResponseDto.getRankname(),
                this.comment,
                this.createDate
        );
    }

    public void updateComment(String comment) {
        this.comment = comment;
    }

    public static MyCommentResponseDto toMyCommentResponseDto(PostComment postComment) {
        return new MyCommentResponseDto(
                postComment.getCommentId(),
                postComment.getComment(),
                postComment.getPost().getTitle(),
                postComment.getCreateDate()
        );
    }
}
