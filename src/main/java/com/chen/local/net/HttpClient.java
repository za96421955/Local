package com.chen.local.net;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.*;
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
public class HttpClient {

    private final CloseableHttpClient client;
    private HttpRequestBase request;
    private String url;
    private String body;
    private int status;
    private String response;

    private HttpClient() {
        this.client = HttpClients.createDefault();
    }

    private HttpClient(String url) {
        this();
        this.url = url;
    }

    /**
     * @description HttpPost 请求
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2025/3/5 14:56
     */
    public static HttpClient post(String url) {
        return new HttpClient(url).request(new HttpPost(url));
    }

    /**
     * @description HttpGet 请求
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2025/3/5 14:56
     */
    public static HttpClient get(String url) {
        return new HttpClient(url).request(new HttpGet(url));
    }

    /**
     * @description HttpPut 请求
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2025/3/5 14:56
     */
    public static HttpClient put(String url) {
        return new HttpClient(url).request(new HttpPut(url));
    }

    /**
     * @description HttpDelete 请求
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2025/3/5 14:56
     */
    public static HttpClient delete(String url) {
        return new HttpClient(url).request(new HttpDelete(url));
    }

    /**
     * @description 设置请求对象
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2025/3/5 14:56
     */
    private HttpClient request(HttpRequestBase request) {
        this.request = request;
        this.request.addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        this.request.addHeader(HttpHeaders.ACCEPT, ContentType.WILDCARD.getMimeType());
        return this;
    }

    /**
     * @description 设置请求头
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2025/3/5 14:57
     */
    public HttpClient header(String name, String value) {
        request.addHeader(name, value);
        return this;
    }

    public HttpClient contentType(String value) {
        this.header(HttpHeaders.CONTENT_TYPE, value);
        return this;
    }

    public HttpClient accept(String value) {
        this.header(HttpHeaders.ACCEPT, value);
        return this;
    }

    public HttpClient authorization(String value) {
        this.header(HttpHeaders.AUTHORIZATION, value);
        return this;
    }

    /**
     * @description 设置请求体
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2025/3/5 14:57
     */
    public HttpClient body(String body) {
        if (request instanceof HttpEntityEnclosingRequestBase) {
            ((HttpEntityEnclosingRequestBase) request).setEntity(new StringEntity(body, ContentType.getByMimeType(
                    request.getFirstHeader(HttpHeaders.CONTENT_TYPE).getValue())));
        }
        this.body = body;
        return this;
    }

    /**
     * @description 执行请求
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2025/3/7 09:46
     */
    private void execute() {
        try (CloseableHttpResponse response = client.execute(request)) {
            status = response.getStatusLine().getStatusCode();
            this.response = EntityUtils.toString(response.getEntity());
            log.debug("[Http请求] url={}, body={}, response={}, status={}, 请求完成",
                    url, body, this.response, status);
        } catch (IOException e) {
            log.debug("[Http请求] url={}, body={}, 请求异常: {}",
                    url, body, e.getMessage(), e);
        }
    }

    /**
     * @description 获取响应状态
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2025/3/5 14:57
     */
    public int asStatus() {
        if (status > 0) {
            return status;
        }
        this.execute();
        return status;
    }

    /**
     * @description 获取字符串结果
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2025/3/5 14:57
     */
    public String asString() {
        if (StringUtils.isNotBlank(response)) {
            return response;
        }
        this.execute();
        return response;
    }

    /**
     * @description 表单请求
     * <p> <功能详细描述> </p>
     *
     * @author 陈晨
     * @date 2024/5/21 19:15
     */
    public String asFromData() {
        return this.header(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.getMimeType())
                .asString();
    }

    public static void main(String[] args) {
        String result = HttpClient.post("https://api.deepseek.com/chat/completions")
                .authorization("Bearer sk-ef2a203479314f71be99b5da4c54a64d")
                .body("{\n" +
                        "  \"messages\": [\n" +
                        "    {\n" +
                        "      \"content\": \"You are a helpful assistant\",\n" +
                        "      \"role\": \"system\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"content\": \"Hi\",\n" +
                        "      \"role\": \"user\"\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"model\": \"deepseek-chat\",\n" +
                        "  \"frequency_penalty\": 0,\n" +
                        "  \"max_tokens\": 2048,\n" +
                        "  \"presence_penalty\": 0,\n" +
                        "  \"response_format\": {\n" +
                        "    \"type\": \"text\"\n" +
                        "  },\n" +
                        "  \"stop\": null,\n" +
                        "  \"stream\": false,\n" +
                        "  \"stream_options\": null,\n" +
                        "  \"temperature\": 1,\n" +
                        "  \"top_p\": 1,\n" +
                        "  \"tools\": null,\n" +
                        "  \"tool_choice\": \"none\",\n" +
                        "  \"logprobs\": false,\n" +
                        "  \"top_logprobs\": null\n" +
                        "}")
                .asString();
        System.out.println(result);
    }

}


