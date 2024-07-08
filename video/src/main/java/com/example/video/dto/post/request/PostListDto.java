package com.example.video.dto.post.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostListDto {

    private String sort;
    private List<String> category;
}
