package com.example.video.entity;

import jakarta.persistence.*;
import lombok.Getter;


@Entity
@Getter
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="category_id")
    private Long categoryId;

    private String category;
}
