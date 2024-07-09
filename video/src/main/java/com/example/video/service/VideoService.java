package com.example.video.service;

import com.example.video.api.UserRestApi;
import com.example.video.dto.post.response.VideoResponseDto;
import com.example.video.dto.user.response.UserInfoDto;
import com.example.video.entity.Post;
import com.example.video.entity.Video;
import com.example.video.repository.PostRepository;
import com.example.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;
    private final UserRestApi userRestApi;

    // 유저 정보 openfeign으로 가져와 값 반환해야 함
    public VideoResponseDto getVideo(Long videoId) {
        Video video = videoRepository.findByIdFetch(videoId).orElseThrow(() -> new IllegalArgumentException("없는 비디오 입니다."));

        Post post = video.getPost();
        post.plusViews();

        UserInfoDto userInfoDto = userRestApi.userInfo(video.getPost().getUserId());

        videoRepository.save(video);

        return video.toVideoReponseDto(video, userInfoDto);
    }

}
