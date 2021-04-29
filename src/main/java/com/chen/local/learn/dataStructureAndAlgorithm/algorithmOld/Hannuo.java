package com.chen.local.learn.dataStructureAndAlgorithm.algorithmOld;

/**
 * 汉诺塔
 * @author 陈晨
 */
public class Hannuo {
	public void show(int n) {
		run(n, "a", "b", "c");
	}

	/**
	 * 运行
	 * @param n
	 * @param a
	 * @param b
	 * @param c
	 */
	public void run(int n, String a, String b, String c) {
		if (n == 1) {
			System.out.println(a+" ---> "+c);
		} else {
			n--;
			run(n, a, c, b);
			System.out.println(a+" ---> "+c);
			run(n, b, a, c);
		}
	}
	
	public static void main(String[] args) {
		Hannuo h = new Hannuo();
		h.show(5);
	}
}
