package com.chen.local.base.services;

import com.chen.local.base.Result;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 抽象服务容器
 * <p>〈功能详细描述〉</p>
 * 
 * @author	陈晨
 * @date	2018年6月8日 下午5:28:11
 * @see		[相关类/方法]（可选）
 * @since	[产品/模块版本]（可选）
 */
@Service
public class AbstractServiceContext implements ApplicationContextAware {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ApplicationContext applicationContext;
    private Map<String, List<AbstractService>> serviceMap = new HashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    @PostConstruct
    public void init() {
        Map<String, AbstractService> map = applicationContext.getBeansOfType(AbstractService.class);
        if (map == null || map.isEmpty()) {
        	return;
        }
		for (Entry<String, AbstractService> entry : map.entrySet()) {
			String key = entry.getValue().getServiceCode() + "@" + entry.getValue().getVersion();
			List<AbstractService> serviceList = serviceMap.get(key);
			if (CollectionUtils.isEmpty(serviceList)) {
				serviceMap.put(key, new ArrayList<>());
				serviceList = serviceMap.get(key);
			}
			serviceList.add(entry.getValue());
		}
    }
    
    /**
     * 获取接口服务
     * <p>〈功能详细描述〉</p>
     * 
     * @author	陈晨
     * @param serviceCode
     * @param version
     * @return
     */
	public AbstractService getService(String serviceCode, String version) {
		List<AbstractService> serviceList = serviceMap.get(serviceCode + "@" + version);
		if (CollectionUtils.isEmpty(serviceList)) {
			return null;
		}
		return serviceList.get(0);
    }

    /**
     * @description 获取接口服务集合
     * <p>〈功能详细描述〉</p>
     *
     * @auther  陈晨
     * @date    2019/5/23 14:48
     * @param   serviceCode, version
     */
	public List<AbstractService> getServiceList(String serviceCode, String version) {
		return serviceMap.get(serviceCode + "@" + version);
	}
	
	/**
	 * 调用服务
	 * <p>〈功能详细描述〉</p>
	 * 
	 * @author	陈晨
	 * @param serviceCode
	 * @param version
	 * @param param
	 * @return
	 */
	public Result transferService(String serviceCode
			, String version, Object param) {
		if (serviceCode == null || StringUtils.isBlank(version) || param == null)
			return null;
		// 获取服务
		AbstractService service = this.getService(serviceCode, version);
		if (service == null) {
			logger.error("[transferService] service={}, version={}, 服务不存在"
					, serviceCode, version);
			throw new RuntimeException("service[" + serviceCode + "], version[" + version + "], 服务不存在");
		}
		// 服务调用
		Result result = service.execute(param);
		logger.debug("[transferService] service={}, version={}, param={}, 【{}】完成"
				, serviceCode, version, param, serviceCode);
		return result;
	}

}


