package com.chen.local.learn.dataStructureAndAlgorithm.algorithmOld;

/**
 * 排序
 * @Description: TODO<br>
 * @Title: Order<br>
 * @Package: chen.calculate.Order<br>
 * @author: 陈晨<br>
 * @date: 2016年2月15日<br>
 * --------------------------------------------------<br>
 * 修改人			修改日期			修改描述<br>
 * 陈晨			2016年2月15日			创建<br>
 * --------------------------------------------------<br>
 * @version: Ver1.0<br>
 *
 */
public class Order {
	public void order(Integer[] args, boolean isAsc) {
		if (args == null || args.length <= 0)
			return;

		if (args.length <= 2) {
			int tmp;
			if ((isAsc && args[0] > args[1])
					|| (!isAsc && args[0] < args[1])) {
				tmp = args[0];
				args[0] = args[1];
				args[1] = tmp;
			}
		}
	}
}
