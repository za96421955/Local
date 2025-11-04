package com.chen.local.ylp;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @description SQL输出
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @date 2024/8/20 18:31
 */
@Slf4j
public class SqlOutput {

    // 需要处理的SQL语句
    private static final String SQL = "INSERT INTO `t_base_small_category` (`id`, `big_category_id`,`code`, `name`,`create_time`, `update_time`) VALUES('%s','%s','%s','%s',NOW(),NOW());\n";
    private static final String[] FILTER = {"gateway-acn01", "motion-aq2"};

    public static void main(String[] args) throws Exception {
        File file = new File("/Users/chenchen/Desktop/sql.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            if (StringUtils.isBlank(line)) {
                continue;
            }
            JSONObject json = JSONObject.parseObject(line);
            System.out.printf(SQL, json.get("id"), json.get("bigCategoryId"), json.get("code"), json.get("name"));
        }
        reader.close();
    }

}


