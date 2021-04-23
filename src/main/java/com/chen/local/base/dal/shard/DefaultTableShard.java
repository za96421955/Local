package com.chen.local.base.dal.shard;

import com.chen.local.base.utils.HashUtil;

import java.util.List;

/**
 * @description 默认Hash分表策略
 * <p>
 *     ${defaultHash('tableNamePrefix', shardTblKey, 32)}
 * </p>
 *
 * @auther  陈晨
 * @date    2019/11/10 15:53
 */
public class DefaultTableShard {
    public static final String METHOD_NAME = "defaultHash";

    public Object exec(List arguments) {
        if (arguments == null || arguments.size() < 2) {
            throw new RuntimeException("PrismTableShard Arguments Size Error");
        }
        // 3参数: tableNamePrefix + hash(shardTblKey, shardCount)
        String prefix = arguments.get(0).toString();
        String shardTblKey = arguments.get(1).toString();
        int shardCount = Integer.parseInt(arguments.get(2).toString());
        long hash = HashUtil.hash(shardTblKey, shardCount);
        // 返回分表名
        return prefix + hash;
    }

}


