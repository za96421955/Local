package com.chen.local.base.scm;

import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @description SCM缓存
 * <p>〈功能详细描述〉</p>
 *
 * @auther  陈晨
 * @date    2019/12/3 17:25
 */
public class SCMCache {
    // Map<path, Map<key, value>>
    private static final Map<String, Map<String, String>> cacheMap = new HashMap<>();

    public static Set<String> getKeySet(String path) {
        if (!cacheMap.containsKey(path)) {
            return Collections.emptySet();
        }
        return cacheMap.get(path).keySet();
    }

    /**
     * @description 获取SCM配置
     * <p>〈功能详细描述〉</p>
     *
     * @auther  陈晨
     * @date    2019/12/4 15:17
     * @param   path, key
     */
    public static String get(String path, String key) {
        if (!cacheMap.containsKey(path) || key == null) {
            return "";
        }
        return cacheMap.get(path).get(key);
    }

    /**
     * @description 获取SCM二级配置
     * <p>
     *     exp:
     *     scm key: timeout.queryDemo
     *     调用: PrismSCMCache.get(PrismSCMConfigEnum.TIMEOUT, "queryDemo")
     * </p>
     *
     * @auther  陈晨
     * @date    2019/12/4 15:17
     * @param   key, secondName
     */
    public static String get(String path, String key, String secondName) {
        if (!cacheMap.containsKey(path)
                || key == null
                || StringUtils.isBlank(secondName)) {
            return "";
        }
        return cacheMap.get(path).get(key + "." + secondName);
    }

    public synchronized static void put(String path, String key, String value) {
        if (StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
            return;
        }
        if (!cacheMap.containsKey(path)) {
            cacheMap.put(path, new HashMap<>());
        }
        cacheMap.get(path).put(key, value);
    }

    public synchronized static void remove(String path, String key) {
        if (!cacheMap.containsKey(path) || StringUtils.isBlank(key)) {
            return;
        }
        cacheMap.get(path).remove(key);
    }

}


