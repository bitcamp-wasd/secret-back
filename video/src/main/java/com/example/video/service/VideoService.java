package com.example.video.service;

import com.example.video.dto.post.response.VideoResponseDto;
import com.example.video.entity.Video;
import com.example.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;

//    public VideoResponseDto getVideo(Long videoId) {
//        Video video = videoRepository.findByIdFetch(videoId).orElseThrow(() -> new IllegalArgumentException("없는 비디오 입니다."));
//
//        return ;
//    }
}
