package com.example.video.util;

import java.util.UUID;

public class Util {

    /**
     * uuid generate
     * @return 랜덤 uuid값 반환
     */
    public static String generateUuid() {
        return UUID.randomUUID().toString();
    }
}
