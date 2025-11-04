package com.chen.local.ylp;

/**
 * @Description : 分片算法
 * @Author : yan
 * @Date: 2020/12/22
 */
public class CommonPreciseShardingAlgorithm {
    private static boolean isEmpty(Object value) {
        if (value instanceof Long) {
            long v = (long) value; 
            return v == 0;
        }
        return value == null;
    }

    public static boolean is2Power(int length) {
        return (length & length - 1) == 0;
    }

    public static Integer getShardingIndex(int length, Object value) {
        Integer index = null;
//        value = AESUtil.encrypt((String) value);
		System.out.println(value);
        if (isEmpty(value)) {
            return null;
        } else {
            int h;
            int hash = (h = value.hashCode()) ^ h >>> 16;
			System.out.println(value.hashCode());

            if (is2Power(length)) {
                index = length - 1 & hash;
            } else {
                index = Math.floorMod(hash, length);
            }
        }
        return index;
    }

    public static void main(String[] args) {
        Integer shardingIndex = getShardingIndex(32, "00000000000000000000780f772968a2");
//        Integer shardingIndex = getShardingIndex(32, "00A0410501055211875645304114657466");
        System.out.println("数据所在的分表 ====> "+ shardingIndex);
    }

}


