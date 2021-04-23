package com.chen.local.base.dal.sql;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @version v1.0
 * @Description: 排序对象
 * <p>〈功能详细描述〉</p>
 *
 * @ClassName SortObject
 * @author 陈晨(17061934)
 * @date 2019/11/19 10:18
 */
@Getter
@Setter
@ToString
public class SortObject implements Serializable, Comparable<SortObject> {

    private SortColumn sort;
    private Object obj;

    public SortObject(SortColumn sort, Object obj) {
        this.sort = sort;
        this.obj = obj;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public int compareTo(SortObject after) {
        if (this.sort == null || after == null || after.getSort() == null) {
            return 0;
        }
        return this.getSort().compareTo(after.getSort());
    }

}


