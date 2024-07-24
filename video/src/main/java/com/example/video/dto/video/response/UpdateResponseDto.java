package com.example.video.dto.video.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UpdateResponseDto {

    private URL thumbnailPresignedUrl;
    private List<URL> sheetMusicPresignedUrl;
}
