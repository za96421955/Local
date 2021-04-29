/*
 * Copyright (C), 2002-2017, 苏宁易购电子商务有限公司
 * FileName: SpatailDistance.java
 * Author:   陈晨(17061934)
 * Date:     2018年7月21日 上午11:05:09
 * Description: // 模块目的、功能描述      
 * History:		// 修改记录
 * <author>		<time>						<version>		<desc>
 * 17061934		2018年7月21日 上午11:05:09		1.0				TODO
 */
package com.chen.local.learn.dataStructureAndAlgorithm.algorithmOld;

import java.util.ArrayList;
import java.util.List;

/**
 * 空间距离
 * <p>〈功能详细描述〉</p>
 * 
 * @author	陈晨(17061934)
 * @date	2018年7月21日 上午11:05:13
 * @see		[相关类/方法]（可选）
 * @since	[产品/模块版本]（可选）
 */
public class SpatailDistance {

	public static void main(String[] args) {
		Point point = new Point(0D, 0D, 0D);
		System.out.println("====================================");
		List<Point> pointList = point.pointTo(1D);
		System.out.println("size: " + pointList.size());	// size: 4163
		for (Point p : pointList) 
			System.out.println(p);
		
		System.out.println("====================================");
		// {x: 0.9, y: 0.40000000000000013, z: 0.10000000000000014}
		// 0.9899494936611667
		// {x: -0.8999999999999998, y: -0.3999999999999999, z: 0.10000000000000014}
		// 0.9899494936611662
		// {x: -0.8999999999999998, y: 0.10000000000000014, z: -0.19999999999999987}
		// 0.9273618495495701
		// {x: 0.5000000000000001, y: 0.8, z: 0.20000000000000015}
		// 0.9643650760992957
		Point point2 = new Point(0.5000000000000001, 0.8, 0.20000000000000015);
		System.out.println(point.distanceTo(point2));
	}
	
	public static class Point {
		private double x;
		private double y;
		private double z;
		
		public Point(double x, double y, double z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		
		@Override
		public String toString() {
			return "{x: " + x + ", y: " + y + ", z: " + z + "}";
		}

		public double distanceTo(Point point) {
			double distanceX = this.x - point.x;
			double distanceY = this.y - point.y;
			double distanceZ = this.z - point.z;
			return Math.sqrt(distanceX * distanceX
					+ distanceY * distanceY
					+ distanceZ * distanceZ);
		}
		
		public List<Point> pointTo(double distance) {
			List<Point> pointList = new ArrayList<Point>();
			double squ = distance * distance;
			double space = 0.1D;
			for (double x = distance * -1; x <= distance; x += space) {
				double squX = x * x;
				for (double y = distance * -1; y <= distance; y += space) {
					double squY = y * y;
					for (double z = distance * -1; z <= distance; z += space) {
						double squZ = z * z;
						if ((squ - (squX + squY + squZ)) < 0) continue;
						pointList.add(new Point(this.x - x, this.y - y, this.z - z));
					}
				}
			}
			return pointList;
		}
		
	}

}


