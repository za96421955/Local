package com.chen.local.base.dal.shard;

import com.chen.local.base.scm.SCMCache;
import com.chen.local.base.utils.HashUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @description 系统分库策略
 * <p>〈功能详细描述〉</p>
 *
 * @auther  陈晨
 * @date    2019/11/9 15:39
 */
public final class DefaultDbShard extends AbstractShard {
	private static final int DEFAULT_SHARD_COUNT = 9;

	private static int shardCount;
	static {
		String shardCount = SCMCache.get("/context.conf", "dbShardCount");
		if (StringUtils.isBlank(shardCount)) {
			DefaultDbShard.shardCount = DEFAULT_SHARD_COUNT;
		} else {
			DefaultDbShard.shardCount = Integer.parseInt(shardCount);
		}
	}

    private DefaultDbShard() {}

	public static int getShardCount() {
    	return DefaultDbShard.shardCount;
	}

    /**
     * @description 获取分库索引
     * <p></p>
     *
     * @auther  陈晨
     * @date    2019/11/9 15:40
     * @param   shardDBKey
     */
    public static long getShardDBIndex(String shardDBKey) {
    	if (StringUtils.isBlank(shardDBKey)) {
    		throw new RuntimeException("can not found shardDBKey");
		}
    	return HashUtil.hash(shardDBKey, DefaultDbShard.getShardCount());
    }

	public static void main(String[] args) {
		long dbIndex = DefaultDbShard.getShardDBIndex(HashUtil.getAssginHashKey(12));
		System.out.println(dbIndex);
	}

}


