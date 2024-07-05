package com.example.video.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class PostComment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="comment_id")
    private Long commentId;

    @Column(name="create_date")
    private LocalDate createDate;

    @Column(name="user_id")
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

    private String comment;
}
