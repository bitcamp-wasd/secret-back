package com.example.video.global.util;

import java.util.UUID;

public class Util {

    /**
     * uuid generate
     * @return 랜덤 uuid값 반환
     */
    public static String generateUuid() {
        return UUID.randomUUID().toString();
    }

    public static String generateUuid(String fileExtention) {
        return UUID.randomUUID().toString() + "." + fileExtention;
    }

    /**
     * 파일 확장자 반환
     * @param fileName
     * @return
     */
    public static String getExtension(String fileName) {
        String extension = fileName.substring(fileName.lastIndexOf(".")+1);
        return extension;
    }
}
