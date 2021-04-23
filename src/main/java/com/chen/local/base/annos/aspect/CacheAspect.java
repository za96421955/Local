package com.chen.local.base.annos.aspect;


import com.chen.local.base.annos.Cache;
import com.chen.local.base.utils.BeanUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

/**
 * @description Redis自动过期缓存
 * <p>〈功能详细描述〉</p>
 *
 * @auther  陈晨
 * @date    2019/8/19 15:04
 */
@Aspect
@Component
public class CacheAspect {
    private static final String LOG_MARK = "Redis自动过期缓存";
    private static final String PREFIX = "Cache:";
    private static final String ANTI_PENETRATION_VALUE = "@@A@@P@@V@@";

    private static Logger logger = LoggerFactory.getLogger(CacheAspect.class);

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Pointcut("@annotation(com.chen.local.base.annos.Cache)")
    public void pointCut() {}

    @Around("pointCut()")
    public Object around(ProceedingJoinPoint point) {
        // 获取被代理方法信息
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        String clazzName = point.getTarget().getClass().getSimpleName();
        String methodName = method.getName();
        Type returnType = method.getGenericReturnType();
        if ("void".equalsIgnoreCase(returnType.toString())) {
            try {
                return point.proceed();
            } catch (Throwable e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e.getMessage());
            }
        }

        // 设置Key
        String redisKey = PREFIX + clazzName + ":" + methodName;
        redisKey = this.appendKey(redisKey, point.getArgs());

        // get anno params
        int expireTime = method.getAnnotation(Cache.class).expireTime();
        boolean isAutoRefresh = method.getAnnotation(Cache.class).isAutoRefresh();
        int penetrateTime = method.getAnnotation(Cache.class).penetrateTime();
        logger.info("【{}】redisKey={}, expireTime={}, isAutoRefresh={}, penetrateTime={}"
                , LOG_MARK, redisKey, expireTime, isAutoRefresh, penetrateTime);

        // step 1: 获取Redis缓存
        Object redisResult = this.getCache(returnType, redisKey, expireTime, isAutoRefresh, penetrateTime);
        // 缓存防穿透
        if (penetrateTime > 0 && ANTI_PENETRATION_VALUE.equals(redisResult)) {
            return null;
        }
        if (redisResult != null) {
            return redisResult;
        }

        // step 2: 未获取到缓存, 则刷新(同步)
        return this.refreshCache(point, returnType, redisKey, expireTime, isAutoRefresh, penetrateTime);
    }

    /**
     * @description step 1:获取Redis缓存
     * <p>〈功能详细描述〉</p>
     *
     * @auther  陈晨
     * @date    2019/12/10 15:12
     * @param   returnType, redisKey, expireTime, isAutoRefresh, penetrateTime
     */
    private Object getCache(Type returnType, String redisKey
            , int expireTime, boolean isAutoRefresh, int penetrateTime) {
        try {
            return this.getResultFromRedis(redisKey, expireTime, isAutoRefresh
                    , penetrateTime, returnType);
        } catch (Throwable e) {
            logger.error("【{}】redisKey={}, expireTime={}, isAutoRefresh={}, 获取Redis缓存, {}"
                    , LOG_MARK, redisKey, expireTime, isAutoRefresh
                    , e.getMessage(), e);
        }
        return null;
    }

    /**
     * @description 刷新缓存(同步)
     * <p>〈功能详细描述〉</p>
     *
     * @auther  陈晨
     * @date    2019/12/10 15:55
     * @param   point, returnType, redisKey, expireTime, isAutoRefresh, penetrateTime
     */
    private synchronized Object refreshCache(ProceedingJoinPoint point, Type returnType
            , String redisKey, int expireTime, boolean isAutoRefresh, int penetrateTime) {
        // step 1: 获取Redis缓存
        Object redisResult = this.getCache(returnType, redisKey, expireTime, isAutoRefresh, penetrateTime);
        // 缓存防穿透
        if (penetrateTime > 0 && ANTI_PENETRATION_VALUE.equals(redisResult)) {
            return null;
        }
        if (redisResult != null) {
            return redisResult;
        }

        // step 2: 执行方法, 获取查询结果
        Object result;
        try {
            result = point.proceed();
            logger.info("【{}】 redisKey={}, result={}, 获取查询结果"
                    , LOG_MARK, redisKey, result);
        } catch (Throwable e) {
            logger.error("【{}】redisKey={}, expireTime={}, isAutoRefresh={}, 执行方法, 获取查询结果异常, {}"
                    , LOG_MARK, redisKey, expireTime, isAutoRefresh
                    , e.getMessage(), e);
            throw new RuntimeException(e.getMessage());
        }

        // step 3: 缓存结果
        try {
            this.cacheResult2Redis(redisKey, expireTime, penetrateTime, result);
        } catch (Throwable e) {
            logger.error("【{}】redisKey={}, expireTime={}, isAutoRefresh={}, 记录Redis缓存异常, {}"
                    , LOG_MARK, redisKey, expireTime, isAutoRefresh
                    , e.getMessage(), e);
        }
        return result;
    }

    /**
     * 根据key 解析参数
     * @param key
     * @param args
     * @return
     */
    private String appendKey(String key, Object[] args) {
        StringBuilder sb = new StringBuilder(key);
        for (Object arg : args) {
            sb.append(":");
            if (arg == null) {
                sb.append("null");
            } else {
                sb.append(arg.hashCode());
            }
        }
        return sb.toString();
    }

    /**
     * @description 获取缓存数据
     * <p>〈功能详细描述〉</p>
     *
     * @auther  陈晨
     * @date    2020/1/4 16:27
     * @param   redisKey, expireTime, isAuthRefresh, penetrateTime, clazz
     */
    private Object getResultFromRedis(String redisKey, int expireTime
            , boolean isAuthRefresh, int penetrateTime, Type returnType) {
        logger.info("【{}】redisKey={}, expireTime={}, isAuthRefresh={}, returnType={}, 获取Redis缓存"
                , LOG_MARK, redisKey, expireTime, isAuthRefresh, returnType);
        String cache = redisTemplate.opsForValue().get(redisKey);
        if (cache == null) {
            return null;
        }
        // 缓存防穿透
        if (penetrateTime > 0 && ANTI_PENETRATION_VALUE.equals(cache)) {
            return cache;
        }

        // 刷新过期时间
        if (isAuthRefresh && expireTime > 0) {
            redisTemplate.expire(redisKey, expireTime, TimeUnit.SECONDS);
            logger.info("【{}】redisKey={}, expireTime={}, returnType={}, 刷新过期时间"
                    , LOG_MARK, redisKey, expireTime, returnType);
        }

        Object result = null;
        try {
            result = BeanUtil.stringToBean(cache, returnType.getClass());
        } catch (Exception e) {
            // 解决之前缓存序列化遗留历史数据(序列化数据)
            redisTemplate.delete(redisKey);
            logger.error("【{}】redisKey={}, expireTime={}, isAuthRefresh={}, returnType={}" +
                            ", 缓存反序列化失败, 移除缓存, {}"
                    , LOG_MARK, redisKey, expireTime, isAuthRefresh, returnType
                    , e.getMessage(), e);
        }
        logger.info("【{}】redisKey={}, expireTime={}, isAuthRefresh={}, returnType={}, 获取Redis缓存成功"
                , LOG_MARK, redisKey, expireTime, isAuthRefresh, returnType);
        return result;
    }

    /**
     * @description 缓存结果到redis
     * <p>〈功能详细描述〉</p>
     *
     * @auther  陈晨
     * @date    2020/1/4 16:24
     * @param   redisKey, expireTime, penetrateTime, result
     */
    private void cacheResult2Redis(String redisKey, int expireTime, int penetrateTime, Object result) {
        logger.info("【{}】redisKey={}, expireTime={}, 序列化Redis缓存"
                , LOG_MARK, redisKey, expireTime);
        String enduranceCache;
        int expireTimeCache = expireTime;
        if (result == null) {
            if (penetrateTime <= 0) {
                return;
            }
            // 缓存防穿透
            enduranceCache = ANTI_PENETRATION_VALUE;
            // 防击穿时间3秒
            expireTimeCache = penetrateTime;
        } else {
            // 结果序列化, 记录缓存, 设置过期时间
            enduranceCache = BeanUtil.beanToString(result);
        }

        redisTemplate.opsForValue().set(redisKey, enduranceCache);
        if (expireTimeCache > 0) {
            redisTemplate.expire(redisKey, expireTimeCache, TimeUnit.SECONDS);
        }
        logger.info("【{}】redisKey={}, expireTime={}, 序列化Redis缓存, 设置过期时间完成"
                , LOG_MARK, redisKey, expireTimeCache);
    }

}


