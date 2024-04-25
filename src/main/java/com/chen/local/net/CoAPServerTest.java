package com.chen.local.net;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.coap.CoAP;
import org.eclipse.californium.core.server.resources.CoapExchange;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * TODO
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2024/4/24
 */
public class CoAPServerTest {

    public static void main(String[] args) {
        CoapServer server = new CoapServer(8443);// 主机为localhost 端口为默认端口5683
        server.add(new CoapResource("hello") {// 创建一个资源为hello 请求格式为 主机：端口\hello
            @Override
            public void handleGET(CoapExchange exchange) { // 重写处理GET请求的方法
                exchange.respond(CoAP.ResponseCode.CONTENT, "Hello CoAP!This is from Java coap server");
            }
        });
        server.add(new CoapResource("time") { // 创建一个资源为time 请求格式为 主机：端口\time
            @Override
            public void handleGET(CoapExchange exchange) {
                Date date = new Date();
                exchange.respond(CoAP.ResponseCode.CONTENT, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
            }
        });
        server.start();
    }

}


