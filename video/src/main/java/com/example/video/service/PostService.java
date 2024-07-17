package com.example.video.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.example.video.dto.auth.UserAuth;
import com.example.video.dto.post.request.PostRegisterDto;
import com.example.video.dto.post.request.PostSortDto;
import com.example.video.dto.post.response.*;
import com.example.video.entity.Category;
import com.example.video.entity.Post;
import com.example.video.entity.SheetMusic;
import com.example.video.entity.Video;
import com.example.video.global.util.Util;
import com.example.video.repository.*;
import jakarta.persistence.PrePersist;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;



import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class PostService {

    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final SheetMusicRepository sheetMusicRepository;

    private final AmazonS3 amazonS3;

    @Value("${ncp.object-storage.videoBucket}")
    private String videoBucket;

    @Value("${ncp.object-storage.thumbnailBucket}")
    private String thumbnailBucket;

    @Value("${ncp.object-storage.sheetMusicBucket}")
    private String sheetMusicBucket;

    /**
     * 게시물, 동영상, 악보를 DB에 등록하고
     * 프론트에서 해당 동영상 파일과 이미지 파일을 Object Storage에 등록할 수 있도록
     * Presigned URL 발급
     * @param postRegisterDto
     * @return 각 동영상, 이미지의 Presigned URL 반환
     */
    @PrePersist
    public PostRegisterResponseDto posting(UserAuth user, PostRegisterDto postRegisterDto) {

        // 파일 별 uuid 생성
        String videoUuid = postRegisterDto.getVideoName() != null ? Util.generateUuid(): null;
        String thumbnailUuid = Util.generateUuid(Util.getExtension(postRegisterDto.getThumbnailName()));
        List<String> sheetMusicUuid = postRegisterDto.getSheetMusicName().stream()
                .map((t) -> Util.generateUuid(Util.getExtension(t))).collect(Collectors.toList());

        // 동영상, 썸네일, 악보 presigned url 발급
        URL videoUrl = generateVideoPresignedUrl(videoUuid);
        URL thumbnailUrl = generateImagePresignedUrl(thumbnailUuid, thumbnailBucket);
        List<URL> sheetMusicUrl = sheetMusicUuid.stream()
                .map((t) -> generateImagePresignedUrl(t, sheetMusicBucket))
                .collect(Collectors.toList());


        // video 생성
        Video video = new Video(videoUuid,postRegisterDto.getDescription(), postRegisterDto.getLength());

        // sheetMusic 생성
        List<SheetMusic> sheetMusics = sheetMusicUuid.stream()
                .map((uuid) -> new SheetMusic(video, uuid))
                .collect(Collectors.toList());

        // Post 생성
        Category category = categoryRepository.findByCategory(postRegisterDto.getCategory())
                .orElseThrow(() -> new NullPointerException("category not found"));

        log.info(user);



        Post post = new Post(
                postRegisterDto.getTitle(),
                category,
                thumbnailUuid,
                video,
                user.getUserId(),
                user.getNickName());


        postRepository.save(post);
        sheetMusicRepository.saveAll(sheetMusics);


        return new PostRegisterResponseDto(videoUrl,thumbnailUrl, sheetMusicUrl);
    }



    /**
     * NCP Object storage 업로드를 위한 Presigned
     * @param uuid 동영상 uuid 이름 생성
     * @return Object Storage 접근용 Presigned URL 반환
     */
    public URL generateVideoPresignedUrl(String uuid) {

        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();

        // url 만료 시간 30분
        expTimeMillis += 1000 * 60 * 30;
        expiration.setTime(expTimeMillis);

        // 저장할 bucket 이름, 허용할 RestApi 방법, Presigned url 만료 시간 지정
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(videoBucket, uuid + ".mp4")
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(expiration);

        // 접근 권한 수정
        generatePresignedUrlRequest.addRequestParameter(Headers.S3_CANNED_ACL, CannedAccessControlList.PublicRead.toString());

        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
    }

    /**
     * 이미지 Presigned URL 반환
     * @param imageName 이미지 이름
     * @param bucket 저장할 버킷 이름
     * @return 이미지 저장 Presigned Url 반환
     */
    public URL generateImagePresignedUrl(String imageName, String bucket) {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();

        // url 만료 시간 30분
        expTimeMillis += 1000 * 60 * 5;
        expiration.setTime(expTimeMillis);

        // 저장할 bucket 이름, 허용할 RestApi 방법, Presigned url 만료 시간 지정
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, imageName)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(expiration);

        // 접근 권한 수정
        generatePresignedUrlRequest.addRequestParameter(Headers.S3_CANNED_ACL, CannedAccessControlList.PublicRead.toString());

        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
    }


    /**
     * 무한 스크롤을 위한 게시물 리스트 받기
     * @param pageable
     * @return
     */
    public Slice<Post> getPostList(List<String> categories, Pageable pageable) {

        if(categories.size() == 0)
            return postRepository.findAllFetch(pageable).orElse(null);

        return postRepository.findAllFetch(categories, pageable).orElse(null);
    }

    /**
     * 검색 결과
     * @param search
     * @param categories
     * @param pageable
     * @return
     */
    public List<PostResponseDto> searchPostList(String search, List<String> categories, Pageable pageable) {

        if(categories.size() == 0)
            return postRepository.findByTitle(search, pageable).orElseThrow(() -> new IllegalArgumentException("검색 결과가 없습니다."))
                    .stream()
                    .map(Post::toPostResponseDto)
                    .toList();
        
        return postRepository.findByTitle(search, categories ,pageable).orElseThrow(() -> new IllegalArgumentException("검색 결과가 없습니다."))
                .stream()
                .map(Post::toPostResponseDto)
                .toList();
    }

    /**
     * 게시물 삭제
     * @param user
     * @param id
     */
    public void deletePost(UserAuth user, Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("잘못된 게시물입니다."));

        // 작성자 본인 확인
        if(post.getUserId() != user.getUserId())
            throw new IllegalArgumentException("해당 게시물을 삭제할 권한이 없습니다.");

        postRepository.delete(post);
    }

    public List<MyPostDto> myPost(UserAuth user, Pageable pageNumber) {

        Long id = user.getUserId();
        Slice<Post> posts = postRepository.findByUserId(id, pageNumber).orElse(null);
        List<MyPostDto> myPosts = posts.stream().map(Post::toMyPostDto).toList();
        return myPosts;
    }


    public VideoApiDto getVideo(Long postId) {
        Post post = postRepository.findByIdFetchVideo(postId).orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));
        return post.toVideoApiDto();
    }

    public UserInfoDto getUserId(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."));
        return post.toUserInfoDto();
    }
}
