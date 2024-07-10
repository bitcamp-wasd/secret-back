package com.example.video.service;

import com.example.video.dto.auth.UserAuth;
import com.example.video.entity.Video;
import com.example.video.entity.VideoLikeList;
import com.example.video.repository.VideoLikeListRepository;
import com.example.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VideoLikeService {

    private final VideoRepository videoRepository;
    private final VideoLikeListRepository videoLikeListRepository;

    /**
     * 좋아요 버튼 클릭 시  좋아요 삭제, 추가
     * @param userAuth
     * @param videoId
     * @return
     */
    public boolean videoLike(UserAuth userAuth, Long videoId) {

        // 리스트 삭제, 삽입 후 좋아요 갯수 -1 or +1
        Video video = videoRepository.findByIdFetch(videoId).orElseThrow(() -> new IllegalArgumentException("video is not found"));

        VideoLikeList like = videoLikeListRepository.findVideoAndUserId(video, userAuth.getUserId()).orElse(null);
        // 리스트 삭제 좋아요 갯수 -1
        if(like != null) {
            videoLikeListRepository.delete(like);
            video.minusLikeCount();
        }
        // 리스트 추가 좋아요 갯수 +1
        else {
            videoLikeListRepository.save(new VideoLikeList(video, userAuth.getUserId()) );
            video.plusLikeCount();
        }

        videoRepository.save(video);
        return true;
    }

    /**
     * 좋아요한 동영상인지 체크
     * @param userAuth
     * @param videoId
     * @return
     */
    public boolean isLike(UserAuth userAuth, Long videoId) {
        if (videoLikeListRepository.existVideoIdAndUserId(videoId, userAuth.getUserId()).orElse(null) == null)
            return false;
        return true;
    }


}
