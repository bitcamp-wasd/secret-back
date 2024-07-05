package com.example.video.dto.post;

import lombok.Getter;
import lombok.Setter;


import java.util.List;


@Getter
@Setter
public class PostRegisterDto {

    // s3 upload file
    private String videoName;
    private String thumbnailName;
    private List<String> sheetMusicName;

    // post data
    private String category;
    private String title;
    private String description;
    private int length;

}
