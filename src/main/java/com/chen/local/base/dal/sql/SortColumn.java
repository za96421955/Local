package com.chen.local.base.dal.sql;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @version v1.0
 * @Description: 排序字段
 * <p>〈功能详细描述〉</p>
 *
 * @ClassName SortColumn
 * @author 陈晨(17061934)
 * @date 2019/11/19 10:18
 */
@Getter
@Setter
@ToString
public class SortColumn implements Serializable, Comparable<SortColumn> {
    private static final long serialVersionUID = 1118881872157541168L;

    private String name;
    private boolean isAsc;
    private Object value;
    private SortColumn nextSort;

    public SortColumn(String name, boolean isAsc) {
        this.name = name;
        this.isAsc = isAsc;
    }

    public SortColumn(String name, boolean isAsc, Object value) {
        this.name = name;
        this.isAsc = isAsc;
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode(){
        return super.hashCode();
    }

    @Override
    public int compareTo(SortColumn after) {
        if (this.value == null || after == null || after.getValue() == null) {
            return 0;
        }

        // 字符比较
        String thisValue = String.valueOf(this.value);
        String afterValue = String.valueOf(after.getValue());
        int compare = thisValue.compareTo(afterValue);
        if (afterValue.length() < thisValue.length()) {
            compare = 1;
        }

        // 数值比较
        String regexNum = "[-]?[0-9]+(\\.[0-9]+)?";
        if (thisValue.matches(regexNum) && afterValue.matches(regexNum)) {
            BigDecimal thisValueDecimal = new BigDecimal(thisValue);
            BigDecimal afterValueDecimal = new BigDecimal(afterValue);
            compare = thisValueDecimal.compareTo(afterValueDecimal);
        }

        if (compare == 0
                && this.nextSort != null
                && after.getNextSort() != null) {
            return this.nextSort.compareTo(after.getNextSort());
        }
        return compare * (isAsc ? 1 : -1);
    }

}


