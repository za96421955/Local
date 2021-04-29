package com.chen.local.learn.dataStructureAndAlgorithm.algorithmOld;

public class Fibnach {
	public void show(int n) {
		for (int i=1; i<=n; i++) {
			for (int j=1; j<=i; j++) {
				System.out.print(get(i, j) + "\t");
			}
			System.out.print("\n");
		}
	}
	
	public int get(int row, int col) {
		if (row < 0 || col < 0 || col > row)
			return 0;
		else if (col == 1 || col == row)
			return 1;
		else {
			return get(row - 1, col - 1) + get(row - 1, col);
		}
	}
}
