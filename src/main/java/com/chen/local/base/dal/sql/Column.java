package com.chen.local.base.dal.sql;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * @version v1.0
 * @Description: SQL字段
 * <p>〈功能详细描述〉</p>
 *
 * @ClassName SortColumn
 * @author 陈晨(17061934)
 * @date 2019/11/19 10:18
 */
@Getter
@Setter
@ToString
public class Column implements Serializable {
    private static final long serialVersionUID = 2797945515201022160L;

    private String completeColumn;
    private String column;
    private String name;
    private Object value;

    public Column(String completeColumn) {
        this.completeColumn = StringUtils.strip(completeColumn);
//        String[] splits = completeColumn.split(" ");
//        StringBuilder column = new StringBuilder();
//        int lastIndex =
//        for () {
//
//        }
//
//        int splitSpaceIndex = this.completeColumn.lastIndexOf(" ");
//        this.column = this.completeColumn.substring(0, splitSpaceIndex);
//        this.name = this.completeColumn.substring(splitSpaceIndex + 1);
    }

}


