package com.chen.local.base.services;

import com.chen.local.base.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @description 抽象容器
 * <p>〈功能详细描述〉</p>
 *
 * @auther  陈晨
 * @date    2020/1/7 17:21
 */
public abstract class AbstractContext {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * @description 获取容器缓存
	 * <p>〈功能详细描述〉</p>
	 *
	 * @auther  陈晨
	 * @date    2020/1/13 17:28
	 */
	public abstract Map<String, Map<Method, Object>> getServiceMap();

	/**
	 * @description 服务是否存在
	 * <p>〈功能详细描述〉</p>
	 *
	 * @auther  陈晨
	 * @date    2020/1/7 12:04
	 * @param   serviceCode, version
	 */
	public boolean containsService(String serviceCode, String version) {
		return this.getServiceMap().containsKey(serviceCode + "@" + version);
	}

	/**
	 * @description 添加服务
	 * <p>〈功能详细描述〉</p>
	 *
	 * @auther  陈晨
	 * @date    2020/1/7 12:04
	 * @param   serviceCode, version, object, method
	 */
	public synchronized void putService(String serviceCode, String version
			, Object object, Method method) {
		if (this.containsService(serviceCode, version)) {
			return;
		}
		Map<Method, Object> service = new HashMap<>();
		service.put(method, object);
		this.getServiceMap().put(serviceCode + "@" + version, service);
	}

	/**
	 * @description 获取服务
	 * <p>〈功能详细描述〉</p>
	 *
	 * @auther  陈晨
	 * @date    2020/1/7 12:04
	 * @param   serviceCode, version
	 */
	public Map<Method, Object> getService(String serviceCode, String version) {
		return this.getServiceMap().get(serviceCode + "@" + version);
    }

    /**
     * @description 获取服务方法
     * <p>〈功能详细描述〉</p>
     *
     * @auther  陈晨
     * @date    2020/1/7 20:49
     * @param   serviceCode, version
     */
    public Method getServiceMethod(String serviceCode, String version) {
		Map<Method, Object> service = this.getService(serviceCode, version);
		if (service == null || service.size() != 1) {
			return null;
		}
		return service.entrySet().iterator().next().getKey();
	}

    /**
     * @description 执行
     * <p>〈功能详细描述〉</p>
     *
     * @auther  陈晨
     * @date    2020/1/7 12:04
     * @param   serviceCode, version, args
     */
    public Result invoke(String serviceCode, String version, Object[] args) {
    	Method method = this.getServiceMethod(serviceCode, version);
		if (method == null) {
			return null;
		}
		Object obj = this.getService(serviceCode, version).get(method);
		try {
			return (Result) method.invoke(obj, args);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

}


