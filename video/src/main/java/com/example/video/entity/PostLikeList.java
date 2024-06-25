package com.example.video.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class PostLikeList {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "list_id")
    private Long listId;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;

    @Column(name="userId")
    private Long userId;



}
