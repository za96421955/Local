//package com.chen.local.services;
//
//import com.alibaba.fastjson.JSON;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.jms.support.converter.MessageConversionException;
//import org.springframework.jms.support.converter.SimpleMessageConverter;
//
//import javax.jms.JMSException;
//import javax.jms.Message;
//import javax.jms.MessageListener;
//import java.io.Serializable;
//import java.io.UnsupportedEncodingException;
//import java.util.Map;
//
///**
// * @description WindQ 消息监听执行
// * <p>〈功能详细描述〉</p>
// * @auther 陈晨
// * @date 2018/9/2 11:37
// */
//public abstract class AbstractWindQListenerService implements MessageListener {
//    // 日志
//    protected Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Override
//    public void onMessage(Message message) {
//        if (message == null) {
//            logger.warn("message is null");
//            return;
//        }
//        logger.debug("[onMessage] message={}, 接收消息", message);
//
//        // 获取消息内容
//        SimpleMessageConverter converter = new SimpleMessageConverter();
//        String messageID = null;
//        String receiveMessage = null;
//        try {
//            messageID = message.getJMSMessageID();
//            Object convertMessage = converter.fromMessage(message);
//            //            if (convertMessage instanceof byte[]) {
//            //                receiveMessage = new String((byte[]) convertMessage, "UTF-8");
//            //            }
//            //            receiveMessage = (String) convertMessage;
//            if (convertMessage instanceof String) {
//                receiveMessage = (String) convertMessage;
//            } else if (convertMessage instanceof byte[]) {
//                //从ESB桥接过来的一般是BytesMessage
//                receiveMessage = new String((byte[]) convertMessage, "UTF-8");
//            } else if (convertMessage instanceof Map) {
//                receiveMessage = JSON.toJSONString(convertMessage);
//            } else if (convertMessage instanceof Serializable) {
//                receiveMessage = convertMessage.toString();
//            }
//        } catch (MessageConversionException e) {
//            logger.error("[onMessage] messageID={}, receiveMessage={}, 消息转换异常, {}"
//                    , messageID, receiveMessage, e.getMessage(), e);
//        } catch (JMSException e) {
//            logger.error("[onMessage] messageID={}, receiveMessage={}, JMS消息监听异常, {}"
//                    , messageID, receiveMessage, e.getMessage(), e);
//        } catch (UnsupportedEncodingException e) {
//            logger.error("[onMessage] messageID={}, receiveMessage={}, charSet={}, 消息编码转换异常, {}"
//                    , messageID, receiveMessage, "UTF-8", e.getMessage(), e);
//        }
//        logger.info("[onMessage] messageID={}, receiveMessage={}, 获取消息内容"
//                , messageID, receiveMessage);
//
//        // 消息处理
//        execute(messageID, receiveMessage);
//    }
//
//    /**
//     * @param msgId
//     * @param content
//     * @return void
//     * @description 执行
//     * <p>〈功能详细描述〉</p>
//     * @auther 陈晨
//     * @date 2018/9/2 11:36
//     */
//    protected abstract void execute(String msgId, String content);
//
//}
//
//
