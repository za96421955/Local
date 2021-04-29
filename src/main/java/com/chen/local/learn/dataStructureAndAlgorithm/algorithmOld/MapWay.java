/*
 * Copyright (C), 2002-2017, 苏宁易购电子商务有限公司
 * FileName: MapWay.java
 * Author:   陈晨(17061934)
 * Date:     2017年10月10日 下午2:38:52
 * Description: // 模块目的、功能描述      
 * History:		// 修改记录
 * <author>		<time>						<version>		<desc>
 * 17061934		2017年10月10日 下午2:38:52		1.0				TODO
 */
package com.chen.local.learn.dataStructureAndAlgorithm.algorithmOld;

public class MapWay {

	public static void main(String[] args) {
		MapWay mapWay = new MapWay();
		Map map = mapWay.getMap(10, 10);
		map.init();
		map.show();
		mapWay.calcu(map.getGrids());
	}
	
	public void calcu(Grid[][] grids) {
		// TODO 
	}
	
	public Map getMap(int x, int y) {
		return new Map(x, y);
	}
	
	private class Map {
		int x;
		int y;
		Grid[][] grids;
		
		public Map(int x, int y) {
			this.x = x;
			this.y = y;
			this.grids = new Grid[x][y];
		}
		
		public Grid[][] getGrids() {
			return grids;
		}
		
		public void init() {
			for (int i=0; i<x; i++) {
				for (int j=0; j<y; j++) {
					int rnd = Double.valueOf(Math.random() * 2).intValue();
					grids[i][j] = new Grid(i, j, rnd == 1);
				}
			}
		}
		
		public void show() {
			System.out.println("x: " + grids.length);
			System.out.println("y: " + grids[0].length);
			System.out.println("\n===================================================");
			for (int i=0; i<x; i++) {
				for (int j=0; j<y; j++) {
					System.out.print(grids[i][j].isFruit() ? "口 " : "果 ");
				}
				System.out.println();
			}
			System.out.println("===================================================\n");
		}
	}
	
	private class Grid {
		int x;
		int y;
		boolean fruit;
		boolean passed;
		
		public Grid(int x, int y, boolean fruit) {
			this.x = x;
			this.y = y;
			this.fruit = fruit;
		}

		public int getX() {
			return x;
		}
		public int getY() {
			return y;
		}
		public boolean isFruit() {
			return fruit;
		}
		public boolean isPassed() {
			return passed;
		}
		public void setPassed(boolean passed) {
			this.passed = passed;
		}
		
	}

}
