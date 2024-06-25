package com.example.video.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UploadService {

    private final AmazonS3 amazonS3;

    @Value("${ncp.object-storage.bucket}")
    private String bucket;



    /**
     * NCP Object storage 업로드를 위한 Presigned
     * @param uuid 동영상 uuid 이름 생성
     * @return Object Storage 접근용 Presigned URL 반환
     */
    public URL generatePresignedUrl(String uuid) {

        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();

        // url 만료 시간 30분
        expTimeMillis += 1000 * 60 * 30;
        expiration.setTime(expTimeMillis);

        // 저장할 bucket 이름, 허용할 RestApi 방법, Presigned url 만료 시간 지정
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucket, uuid)
                        .withMethod(HttpMethod.PUT)
                        .withExpiration(expiration);

        // 접근 권한 수정
        generatePresignedUrlRequest.addRequestParameter(Headers.S3_CANNED_ACL, CannedAccessControlList.PublicRead.toString());

        return amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
    }
    
}
