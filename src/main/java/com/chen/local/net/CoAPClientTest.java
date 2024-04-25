package com.chen.local.net;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

/**
 * TODO
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2024/4/24
 */
public class CoAPClientTest {

    public static void main(String[] args) {
        post();
//        get();
    }

    private static void post() {
        // CoAP服务器地址
//        String coapServerUri = "coaps://127.0.0.1:18443/ola/v1/device/auth";
        String coapServerUri = "coap://127.0.0.1:8443/ola/v1/device/auth";
//        String coapServerUri = "coap://smarthome-mini.189smarthome.com:9001/ola/v1/device/auth";
        // 创建CoapClient实例
        CoapClient client = new CoapClient(coapServerUri);
        // 设置需要发送的数据
        String payload = "{\"registryCode\":\"oh3mGSbdiNFYc95J\",\"Rc\":\"df69\",\"udid\":\"8SUpuksYCQfFu6gd215RSOT0c4kBobQpm1DUmnAx/WU=\",\"mac\":\"4417935e4a36\"}";
        try {
            // 发送请求并接收响应
            CoapResponse response = client.post(payload, MediaTypeRegistry.APPLICATION_JSON);
            // 输出响应结果
            if (response != null) {
                System.out.println("Received response: " + response.getCode() + " " + response.getOptions());
                System.out.println("Payload: " + response.getResponseText());
            } else {
                System.out.println("No response received.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void get() {
        // CoAP服务器地址
        String coapServerUri = "coap://127.0.0.1:5683/hello";
        // 创建CoapClient实例
        CoapClient client = new CoapClient(coapServerUri);
        try {
            // 发送请求并接收响应
            CoapResponse response = client.get();
            // 输出响应结果
            if (response != null) {
                System.out.println("Received response: " + response.getCode() + " " + response.getOptions());
                System.out.println("Payload: " + response.getResponseText());
            } else {
                System.out.println("No response received.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
