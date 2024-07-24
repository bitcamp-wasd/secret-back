package com.example.video.service;

import com.amazonaws.services.s3.AmazonS3;
import com.example.video.api.UserRestApi;
import com.example.video.dto.auth.UserAuth;
import com.example.video.dto.video.request.UpdateRequestDto;
import com.example.video.dto.video.response.UpdateResponseDto;
import com.example.video.dto.video.response.VideoResponseDto;
import com.example.video.dto.user.response.UserInfoDto;
import com.example.video.entity.Category;
import com.example.video.entity.Post;
import com.example.video.entity.SheetMusic;
import com.example.video.entity.Video;
import com.example.video.global.util.Util;
import com.example.video.repository.CategoryRepository;
import com.example.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;
    private final CategoryRepository categoryRepository;
    private final UserRestApi userRestApi;
    private final PostService postService;
    private final AmazonS3 amazonS3;

    @Value("${ncp.object-storage.thumbnailBucket}")
    private String thumbnailBucket;

    @Value("${ncp.object-storage.sheetMusicBucket}")
    private String sheetMusicBucket;

    // 유저 정보 openfeign으로 가져와 값 반환해야 함
    public VideoResponseDto getVideo(Long videoId) {
        Video video = videoRepository.findByIdFetch(videoId).orElseThrow(() -> new IllegalArgumentException("없는 비디오 입니다."));

        Post post = video.getPost();
        post.plusViews();

        UserInfoDto userInfoDto = userRestApi.userInfo(video.getPost().getUserId());

        videoRepository.save(video);

        return video.toVideoReponseDto(video, userInfoDto);
    }

    public Long getPostId(Long videoId) {
        Video video = videoRepository.findByIdFetch(videoId).orElseThrow(() -> new IllegalArgumentException("없는 비디오 입니다."));

        Post post = video.getPost();
        return post.getPostId();
    }

    public UpdateResponseDto updateVideo(UserAuth userAuth, Long videoId, UpdateRequestDto updateRequestDto) throws IllegalAccessException {



        Video video = videoRepository.findByIdFetch(videoId).orElseThrow(() -> new IllegalArgumentException("유효한 동영상이 아닙니다."));

        if(userAuth.getUserId() != video.getPost().getUserId())
            throw new IllegalArgumentException("권한이 없는 요청입니다.");

        Category category =  categoryRepository.findByCategory(updateRequestDto.getCategory()).orElseThrow(() -> new IllegalArgumentException("유효한 카테고리가 아닙니다."));



        URL thumbnailPresignedUrl = null;
        List<URL> sheetMusicPresignedUrl = null;


        if(updateRequestDto.getThumbnail() != null) {
            // Object Storage 삭제
            amazonS3.deleteObject(thumbnailBucket, video.getPost().getThumbnailPath());
            String thumbnailUuid = Util.generateUuid(Util.getExtension(updateRequestDto.getThumbnail()));
            thumbnailPresignedUrl = postService.generateImagePresignedUrl(thumbnailUuid, thumbnailBucket);
            video.getPost().updateThumbnailPath(thumbnailUuid);
        }

        if(updateRequestDto.getSheetMusic() != null) {
            // Object Storage 삭제
            video.getSheetMusicList().stream().forEach((sheetMusic -> {
                amazonS3.deleteObject(sheetMusicBucket, sheetMusic.getPath());
            }));

            List<String> sheetMusicUuid = updateRequestDto.getSheetMusic().stream()
                    .map((t) -> Util.generateUuid(Util.getExtension(t))).collect(Collectors.toList());

            sheetMusicPresignedUrl = sheetMusicUuid.stream()
                    .map((t) -> postService.generateImagePresignedUrl(t, sheetMusicBucket))
                    .collect(Collectors.toList());

            List<SheetMusic> sheetMusics = sheetMusicUuid.stream()
                    .map((uuid) -> new SheetMusic(video, uuid))
                    .collect(Collectors.toList());

            video.updateSheetMusic(sheetMusics);
        }


        video.updateVideo(category, updateRequestDto.getDescription(), updateRequestDto.getTitle());
        videoRepository.save(video);

        return new UpdateResponseDto(thumbnailPresignedUrl, sheetMusicPresignedUrl);

    }
}
