package com.chen.local.base.services;

import com.chen.local.base.annos.WindQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.Destination;
import java.util.Map;

/**
 * @description Windq 监听容器注册器
 * <p>〈功能详细描述〉</p>
 *
 * @auther  陈晨
 * @date    2018/9/2 14:49
 */
@Service
public class WindQListenerContainerRegister implements ApplicationContextAware {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // Spring容器
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

//    @PostConstruct
    public void init() {
        // 将applicationContext转换为ConfigurableApplicationContext
        ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) applicationContext;
        // 获取bean工厂并转换为DefaultListableBeanFactory
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) configurableApplicationContext.getBeanFactory();

        Map<String, Object> map = applicationContext.getBeansWithAnnotation(WindQListener.class);
        if (map == null || map.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            WindQListener anno = entry.getValue().getClass().getAnnotation(WindQListener.class);
            if (!anno.isRegister()) {
                continue;
            }
            String beanId = anno.name() + "ContainerByWindQ";
            if (defaultListableBeanFactory.containsBean(beanId)) {
                continue;
            }
            Destination destination = null;
//            if (anno.isTopic()) {
//                destination = new WindQTopic(anno.name(), anno.subscriber());
//            } else {
//                destination = new WindQQueue(anno.name());
//            }
            // 通过BeanDefinitionBuilder创建bean定义
//            BeanDefinitionBuilder beanBuiler = BeanDefinitionBuilder.genericBeanDefinition(DefaultMessageListenerContainer.class);
            BeanDefinitionBuilder beanBuiler = null;
            // 使用WINDQ原生的连接工程，不要使用cachingConnectionFactory, 因为MLC自己内部有缓存机制
            beanBuiler.addPropertyReference("connectionFactory", "jmsConnectionFactoryByWindQ");
            // 并发consumer的数量: 最小-最大
            beanBuiler.addPropertyValue("concurrency", "1-1");
            // 设置主题
            beanBuiler.addPropertyValue("destination", destination);
            // 设置监听类
            beanBuiler.addPropertyReference("messageListener", entry.getKey());
            // 打开事务, session 类型为 transaction
            beanBuiler.addPropertyValue("sessionTransacted", true);
            // 关闭事务传递, windq是分布式队列, 不支持事务的传递
            beanBuiler.addPropertyValue("exposeListenerSession", false);
            // 注册bean
            defaultListableBeanFactory.registerBeanDefinition(beanId, beanBuiler.getRawBeanDefinition());
            logger.info("[init] topic={}, container={}, 消息监听容器注册完成", anno.name(), beanId);
        }
    }

}


