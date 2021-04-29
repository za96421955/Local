package com.chen.local.learn.dataStructureAndAlgorithm.algorithmOld;

import java.util.ArrayList;
import java.util.List;

public class AllOrder {
	public void show(int n) {
		String[] str = new String[n];
		for (int i=1; i<=n; i++) {
			str[i - 1] = i + "";
		}
		
		for (String s:get(str)) {
			System.out.println(s);
		}
	}
	
	public String[] get(String[] str) {
		if (str.length == 1)
			return str;
		else {
			int len = 1;
			for (int i=1; i<=str.length; i++) {
				len *= i;
			}
			String[] rtn = new String[len];
			String[] next = new String[str.length - 1];
			int index = 0;
			for (int i=0; i<str.length; i++) {
				for (int j=0; j<str.length; j++) {
					if (j < i)
						next[j] = str[j];
					else if (j > i)
						next[j - 1] = str[j];
				}
				next = get(next);
				for (int j=0; j<next.length; j++) {
					rtn[index++] = str[i] + next[j];
				}
			}
			return rtn;
		}
	}
	
	/**
	 * 全排列
	 * @param elements
	 * @return
	 * @author: 陈晨<br>
	 * @date: 2016-12-20<br>
	 */
	public List<String> allSort(List<String> elements) {
		if (elements == null || elements.size() <= 0) 
			return null;
		List<String> rtn = new ArrayList<String>();
		if (elements.size() <= 1) {
			rtn.add(elements.get(0));
		} else {
			// 遍历元素集合, 提取首元素
			for (int i=0; i<elements.size(); i++) {
				String first = elements.remove(i);
				for (String other : allSort(elements)) {
					rtn.add(first + other);
				}
				elements.add(i, first);
			}
		}
		return rtn;
	}
	
	public static void main(String[] args) {
		List<String> datas = new ArrayList<String>();
		datas.add("A");
		datas.add("B");
		datas.add("C");
		datas.add("D");
		datas.add("E");
		datas.add("F");
		datas.add("G");
		datas.add("H");
		datas.add("I");
//		datas.add("J");
//		datas.add("K");
//		datas.add("L");
//		datas.add("M");
//		datas.add("N");
		
		AllOrder order = new AllOrder();
		long time = System.currentTimeMillis();
		List<String> allSort = order.allSort(datas);
		time = System.currentTimeMillis() - time;
		long count = 0;
		for (String sort : allSort) {
			count++;
			System.out.println(sort);
		}
		System.out.println("Count: " + count);
		// 应有结果数
		count = 1;
		for (int i=2; i<=datas.size(); i++) {
			count *= i;
		}
		System.out.println("Count: " + count);
		// 耗时
		System.out.println("Time: " + time + " ms");
	}
	
}
