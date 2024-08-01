package com.example.video.dto.video.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateRequestDto {
    private String thumbnail;
    private String category;
    private String description;
    private String title;
    private List<String> sheetMusic;
}
