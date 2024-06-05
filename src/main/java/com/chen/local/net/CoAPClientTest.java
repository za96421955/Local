package com.chen.local.net;

import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.coap.MediaTypeRegistry;
import org.eclipse.californium.core.coap.Request;
import org.eclipse.californium.core.network.CoapEndpoint;
import org.eclipse.californium.elements.config.Configuration;
import org.eclipse.californium.scandium.DTLSConnector;
import org.eclipse.californium.scandium.config.DtlsConnectorConfig;
import org.eclipse.californium.scandium.dtls.pskstore.AdvancedSinglePskStore;


/**
 * TODO
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2024/4/24
 */
public class CoAPClientTest {

    /**
     * java -jar local-0.0.1-SNAPSHOT.jar "coap://127.0.0.1:8443/ola/v1/device/auth"
     * java -jar local-0.0.1-SNAPSHOT.jar "coap://smarthome-mini.189smarthome.com:9001/ola/v1/device/auth"
     */
    public static void main(String[] args) {
        // CoAP服务器地址
//        String coapServerUri = "coaps://127.0.0.1:18443/ola/v1/device/auth";
//        String coapServerUri = "coaps://127.0.0.1:8443/ola/v1/device/auth";
        String coapServerUri = "coaps://smarthome-mini.189smarthome.com:9004/ola/v1/device/auth";
//        String coapServerUri = "coap://117.83.178.185:9004/ola/v1/device/auth";

//        post(args[0]);
        postDTLS(coapServerUri);
//        get();
    }

    private static void postDTLS(String url) {
        String payload = "{\"registryCode\":\"Tvn5HtTMjQZnJLxf\",\"Rc\":\"df69\",\"udid\":\"8SUpuksYCQfFu6gd215RSOT0c4kBobQpm1DUmnAx/WU=\",\"mac\":\"4417935e4a36\"}";
//        String identity = "temporary";
        String identity = "udid.8SUpuksYCQfFu6gd215RSOT0c4kBobQpm1DUmnAx/WU=";
        String password = "123456";
        try {
            // DTLS 配置
            Configuration config = Configuration.getStandard();
            DtlsConnectorConfig.Builder dtlsConfig = new DtlsConnectorConfig.Builder(config);
            // 设置预共享密钥 (PSK)
            AdvancedSinglePskStore pskStore = new AdvancedSinglePskStore(identity, password.getBytes());
            dtlsConfig.setAdvancedPskStore(pskStore);
            // 创建 DTLS 连接器
            DTLSConnector dtlsConnector = new DTLSConnector(dtlsConfig.build());
            // 使用 DTLS 连接器创建 CoAP 客户端
            CoapClient client = new CoapClient(url);
            client.setTimeout(10000L);
            client.setEndpoint(new CoapEndpoint.Builder().setConnector(dtlsConnector).setConfiguration(config).build());

            // 构造 POST 请求
            Request postRequest = new Request(CoAP.Code.POST);
            postRequest.setType(CoAP.Type.CON);
            postRequest.setPayload(payload);
            // 发送 POST 请求
            CoapResponse response = client.advanced(postRequest);
            if (response != null) {
                System.out.println(response.getResponseText());
            } else {
                System.out.println("没有收到响应。");
            }

//            // 发送 GET 请求
//            CoapResponse response = client.get();
//            if (response != null) {
//                System.out.println(response.getResponseText());
//            } else {
//                System.out.println("没有收到响应。");
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void post(String coapServerUri) {
        // 创建CoapClient实例
        CoapClient client = new CoapClient(coapServerUri);
        client.setTimeout(10000L);
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
