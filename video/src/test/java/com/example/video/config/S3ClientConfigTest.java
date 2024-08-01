package com.example.video.config;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.Bucket;
import com.netflix.discovery.converters.Auto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class S3ClientConfigTest {

    @Autowired
    AmazonS3 s3;

    @Test
    void amazonS3() {

        List<Bucket> list = s3.listBuckets();

        list.forEach((bucket) -> {
            System.out.println(bucket.getName());
        });
    }
}