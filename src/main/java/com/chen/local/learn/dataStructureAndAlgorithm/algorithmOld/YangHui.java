package com.chen.local.learn.dataStructureAndAlgorithm.algorithmOld;

public class YangHui {
	public void show(int n) {
		for (int i=1; i<=n; i++) {
			for (int j=1; j<=i; j++) {
				System.out.print(get(i, j) + "\t");
			}
			System.out.print("\n");
		}
	}
	
	public int get(int row, int col) {
		int remove = (row - 1) / 2;
		int center = remove + 1;
		if (col <= center)
			return col;
		else
			return row - (col - center) - remove;
	}
	
	public static void main(String[] args) {
		YangHui yh = new YangHui();
		yh.show(6);
	}
	
}


