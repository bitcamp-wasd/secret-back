package com.example.video.dto.post.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.net.URL;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostRegisterResponseDto {

    private URL videoPresignedUrl;
    private URL thumbnailPresignedUrl;
    private List<URL> sheetMusicPresignedUrl;

}
