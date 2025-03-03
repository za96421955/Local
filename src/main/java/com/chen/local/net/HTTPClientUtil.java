package com.chen.local.net;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @description HTTP工具
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @date 2024/6/4 18:03
 */
@Slf4j
public abstract class HTTPClientUtil {

    private HTTPClientUtil() {}

    /**
     * @description 关闭 HttpClient实例
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2024/5/21 12:35
     */
    public static void close(CloseableHttpClient httpClient) {
        try {
            httpClient.close();
        } catch (IOException e) {
            log.error("关闭HttpClient实例异常: {}", e.getMessage(), e);
        }
    }

    /**
     * @description 重新创建 HttpClient实例
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2024/5/21 12:36
     */
    public static CloseableHttpClient recreateClient(CloseableHttpClient httpClient) {
        close(httpClient);
        return HttpClients.createDefault();
    }

    /**
     * @description POST请求设置器
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2024/6/4 18:18
     */
    public interface PostRequestSetter {
        String set(HttpPost httpPost);
    }

    /**
     * @description POST请求
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2024/5/21 13:12
     */
    public static String post(CloseableHttpClient httpClient, String url, String body,
                       PostRequestSetter setter) {
        log.info("[POST请求] url={}, body={}, 发起请求", url, body);
        try {
            // 创建HttpPost实例
            HttpPost httpPost = new HttpPost(url);
            // 设置
            setter.set(httpPost);
            // 设置请求体
            StringEntity entity = new StringEntity(body);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            // 执行请求
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                String responseBody = EntityUtils.toString(response.getEntity());
                log.info("[POST请求] url={}, body={}, response={}, status={}, 请求完成",
                        url, body, responseBody, response.getStatusLine().getStatusCode());
                return responseBody;
            }
        } catch (Exception e) {
            log.error("[POST请求] url={}, body={}, 请求异常: {}",
                    url, body, e.getMessage(), e);
            return null;
        }
    }

    /**
     * @description POST请求
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2024/5/21 13:12
     */
    public static String post(CloseableHttpClient httpClient, String url, String body) {
        return post(httpClient, url, body, httpPost -> null);
    }

    /**
     * @description POST表单请求
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2024/5/21 19:15
     */
    public static String postByFromData(CloseableHttpClient httpClient, String url, String fromData,
                                 PostRequestSetter setter) {
        log.info("[POST表单请求] url={}, fromData={}, 发起请求", url, fromData);
        try {
            // 创建HttpPost实例，设置请求URI
            HttpPost httpPost = new HttpPost(url);
            setter.set(httpPost);
            // 设置请求头信息，指定内容类型和字符集
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            // 创建要发送的内容实体，并指定其内容类型和字符集
            StringEntity requestEntity = new StringEntity(fromData, ContentType.APPLICATION_FORM_URLENCODED);
            httpPost.setEntity(requestEntity);
            // 执行请求并获得响应, 并关闭响应对象 (AutoCloseable)
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                String responseBody = EntityUtils.toString(response.getEntity());
                log.info("[POST表单请求] url={}, fromData={}, response={}, status={}, 请求完成",
                        url, fromData, responseBody, response.getStatusLine().getStatusCode());
                return responseBody;
            }
        } catch (Exception e) {
            log.error("[POST表单请求] url={}, fromData={}, 请求异常: {}",
                    url, fromData, e.getMessage(), e);
            return null;
        }
    }

}


