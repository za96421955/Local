package com.chen.local.base.scm;

/**
 * @description 默认获取SCM配置服务
 * <p>〈功能详细描述〉</p>
 *
 * @auther  陈晨
 * @date    2020/1/4 11:11
 */
public class DefaultSCMValueGetServiceImpl implements SCMValueGetService {
    private String path;

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String get(String key) {
        return SCMCache.get(path, key);
    }

}


