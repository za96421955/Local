package com.chen.local.base.services;

import com.chen.local.base.Result;

/**
 * 抽象接口
 * <p>〈功能详细描述〉</p>
 * 
 * @author	陈晨
 * @date	2018年6月8日 下午5:13:10
 * @see		[相关类/方法]（可选）
 * @since	[产品/模块版本]（可选）
 */
public interface AbstractService<T> {
	/**
	 * 版本号
	 * <p>〈功能详细描述〉</p>
	 * 
	 * @author	陈晨
	 * @date	2018年6月8日 下午5:14:12
	 * @see		[相关类/方法]（可选）
	 * @since	[产品/模块版本]（可选）
	 */
	interface VERSION {
		String V_1_0 = "1.0";
	}

	/**
	 * @description 获取日志标记
	 * <p>〈功能详细描述〉</p>
	 *
	 * @auther  陈晨
	 * @date    2020/1/4 18:07
	 */
	String getLogMark();
	
	/**
	 * 获取服务码
	 * <p>〈功能详细描述〉</p>
	 * 
	 * @author	陈晨
	 * @return
	 */
	String getServiceCode();
	
	/**
	 * 获取版本号
	 * <p>〈功能详细描述〉</p>
	 * 
	 * @author	陈晨
	 * @return
	 */
	String getVersion();
	
	/**
	 * 执行
	 * <p>〈功能详细描述〉</p>
	 * 
	 * @author	陈晨
	 * @param	param
	 * @return
	 */
	Result execute(T param);

}


