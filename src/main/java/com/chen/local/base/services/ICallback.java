package com.chen.local.base.services;

/**
 * 回调接口
 * <p>〈功能详细描述〉</p>
 * 
 * @author	陈晨
 * @date	2017年9月5日 上午11:57:25
 * @see		[相关类/方法]（可选）
 * @since	[产品/模块版本]（可选）
 */
public interface ICallback<T> {
	/**
	 * 回调方法
	 * <p>〈功能详细描述〉</p>
	 * 
	 * @author	陈晨
	 * @return
	 */
	T callback();
	
}


