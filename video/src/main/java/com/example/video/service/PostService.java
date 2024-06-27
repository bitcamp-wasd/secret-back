package com.example.video.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.example.video.dto.post.PostRegisterDto;
import com.example.video.dto.post.PostResponseDto;
import com.example.video.entity.Category;
import com.example.video.entity.Post;
import com.example.video.entity.SheetMusic;
import com.example.video.entity.Video;
import com.example.video.global.util.Util;
import com.example.video.repository.CategoryRepository;
import com.example.video.repository.PostRepository;
import com.example.video.repository.SheetMusicRepository;
import com.example.video.repository.VideoRepository;
import jakarta.persistence.PrePersist;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;



import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {

    private final CategoryRepository categoryRepository;
    private final PostRepository postRepository;
    private final VideoRepository videoRepository;
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
    public PostResponseDto posting(PostRegisterDto postRegisterDto) {

        // 파일 별 uuid 생성
        String videoUuid = postRegisterDto.getVideoName() != null ? Util.generateUuid("mp4"): null;
        String thumbnailUuid = Util.generateUuid(Util.getExtension(postRegisterDto.getThumbnailName()));
        List<String> sheetMusicUuid = postRegisterDto.getSheetMusicName().stream()
                .map((t) -> Util.generateUuid()).collect(Collectors.toList());

        // presigned url 발급
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
        Post post = new Post(
                postRegisterDto.getTitle(),
                category,
                thumbnailUuid,
                video,
                videoUuid);


        videoRepository.save(video);
        sheetMusicRepository.saveAll(sheetMusics);
        postRepository.save(post);


        return new PostResponseDto(videoUrl,thumbnailUrl, sheetMusicUrl);
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

    
}
