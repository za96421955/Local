package com.chen.local.base.annos.aspect;

import com.chen.local.base.annos.RequestLimit;
import com.chen.local.base.Result;
import com.chen.local.base.scm.SCMCache;
import com.chen.local.base.utils.GuidUtil;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @description 访问限制
 * <p>〈功能详细描述〉</p>
 *
 * @auther  余昆鹏(19040976)
 */
@Aspect
@Component
public class RequestLimitAspect {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Pointcut("@annotation(com.chen.local.base.annos.RequestLimit)")
    private void pointCut() {}

    @Around("pointCut()")
    public Object requestLimit(ProceedingJoinPoint point) throws Throwable {
        Method method = ((MethodSignature) point.getSignature()).getMethod();
        RequestLimit requestLimit = method.getAnnotation(RequestLimit.class);
        if (!getLimitSwitch()) {
            return point.proceed();
        }
        HttpServletRequest request = ((ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes()).getRequest();
        if (!checkRequestLimit(request, requestLimit.time(), requestLimit.limitCount())) {
            return Result.buildFail(requestLimit.message());
        }
        return point.proceed();
    }

    protected boolean checkRequestLimit(HttpServletRequest request, int time, int limitCount) {
        try {
            String uri = request.getRequestURI();
            // TODO userId随机值
            String userId = GuidUtil.getUUID();
            boolean checkResult = checkLimitCount(userId, uri, time, limitCount);
            if (!checkResult) {
                logger.info("[url访问次数限制],[url:{},用户:{}]超过了限定的次数[{}]次"
                        , uri, userId, limitCount);
                return false;
            }
        } catch (Exception e) {
            logger.error("[url访问次数限制] exception: ", e);
        }
        return true;
    }

    protected boolean checkLimitCount(String userId, String url, int time, int limitCount) {
        url = url.replaceAll("/", "_");
        String key = "REQ_LIMIT:" + url + ":" + userId;
        int requestCount = 0;
        try {
            requestCount = redisTemplate.opsForValue().increment(key, time).intValue();
        } catch (Exception e) {
            logger.error("[url访问次数限制]redis incr error:{}", e);
        }
        return requestCount <= limitCount;
    }

    /**
     * 流控总开关
     *
     * @return
     */
    protected boolean getLimitSwitch() {
        String value = SCMCache.get("/context.conf", "requestLimitSwitch");
        // 未配置, 或非"false", 则认为开关开启
        return StringUtils.isBlank(value) || !"false".equals(value);
    }
}

