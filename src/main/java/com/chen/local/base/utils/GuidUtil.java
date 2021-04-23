package com.chen.local.base.utils;

import java.util.UUID;

/**
 * 生成GUID
 */
public abstract class GuidUtil {

    private GuidUtil() {}

    /**
     * 生成器32位UUID
     */
    public static synchronized String getUUID() {
        String uuid = UUID.randomUUID().toString();
        return uuid.substring(0, 8)
            + uuid.substring(9, 13)
            + uuid.substring(14, 18)
            + uuid.substring(19, 23)
            + uuid.substring(24, 36);
    }

}


