package com.chen.local.net;

import com.alibaba.fastjson.JSONObject;
import io.netty.handler.codec.json.JsonObjectDecoder;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * TODO
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2024/3/29
 */
public class HTTP {
    private static final String SERVICE = "127.0.0.1:7443";
    private static final String URI = "/ola/v1/device/register";

    public static void main(String[] args) throws Exception {
        System.out.println(HTTP.post());
    }

    public static String get() throws IOException {
        String url = "https://" + HTTP.SERVICE + HTTP.URI;
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse resp = client.execute(new HttpGet(url));
        return EntityUtils.toString(resp.getEntity());
    }

    /**
     * 设备注册
     * curl -i -k -H "Accept: application/json" -H "Content-Type: application/json" -X POST https://127.0.0.1:7443/ola/v1/device/register -d '{"udid":"308398d66711","mac":"308398d66711","productId":"AC01"}'
     *
     * 设备注册成功与否
     * 轮询调用，建议8s/次
     * curl -i -k -H "Accept: application/json" -H "Content-Type: application/json" -X POST https://127.0.0.1:7443/ola/v1/device/query/register/status -d '{"registry_code":"cE6Hv8R5lrBTltTo"}'
     *
     * 获取设备支持的Services
     * curl -i -k -H "Accept: application/json" -H "Content-Type: application/json" -X POST https://127.0.0.1:7443/ola/v1/device/services -d '{"devId":"zcHWuXA0ZmuQF41a"}'
     *
     * 设备控制
     * curl -i -k -H "Accept: application/json" -H "Content-Type: application/json" -X POST https://127.0.0.1:7443/ola/v1/device/control -d '{"devId":"zcHWuXA0ZmuQF41a","siid":1,"iid":1,"value":true}'
     *
     * 设备最新属性信息
     * curl -i -k -H "Accept: application/json" -H "Content-Type: application/json" -X POST https://127.0.0.1:7443/ola/v1/device/data/prop -d '{"devId":"zcHWuXA0ZmuQF41a"}'
     *
     * 设备最近事件信息
     * curl -i -k -H "Accept: application/json" -H "Content-Type: application/json" -X POST https://127.0.0.1:7443/ola/v1/device/data/event -d '{"devId":"zcHWuXA0ZmuQF41a"}'
     *
     * 设备Action控制
     * url -i -k -H "Accept: application/json" -H "Content-Type: application/json" -X POST https://127.0.0.1:7443/ola/v1/device/actions -d '{"devId":"zcHWuXA0ZmuQF41a","siid":2,"iid":1,"inputData":{"url":"www.sina.com.cn","version":"v1.01","file_len":512,"md5":"qweasdfrtgyhuj"}}'
     *
     * 查询设备属性
     * curl -i -k -H "Accept: application/json" -H "Content-Type: application/json" -X POST https://127.0.0.1:7443/ola/v1/device/query/properities -d '{"devId":"zcHWuXA0ZmuQF41a","services":[{"siid":1,"properties":[{"iid":1,"timeOut":5000},{"iid":2,"timeOut":5000}]},{"siid":2,"properties":[{"iid":3,"timeOut":5000},{"iid":4,"timeOut":5000}]}]}'
     *
     */
    public static String post() throws Exception {
        JSONObject json = new JSONObject();
        json.put("udid", "308398d66711");
        json.put("mac", "308398d66711");
        json.put("productId", "AC01");

//        json.put("cloud_url", "127.0.0.1:7443");
//        json.put("errcode", "0");
//        json.put("registry_code", "JV3s54WcxbyzAC8T");
//        json.put("uuid", "ovCrHz9Y3SI3eUgx3qY9xpqDfiPuG1woRwIlAKycfNY=");
        StringEntity entity = new StringEntity(json.toString());
        entity.setContentType("application/json");

        String url = "https://" + HTTP.SERVICE + HTTP.URI;
//        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);

        HttpClient client = new SSLClient();
        HttpResponse resp = client.execute(httpPost);
        return EntityUtils.toString(resp.getEntity());
    }

}


