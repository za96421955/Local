package com.chen.local.ylp;

import com.chen.local.base.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 结果处理
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/11/26
 */
@Slf4j
public class HandleResult {

    public static void main(String[] args) throws Exception {
        HandleResult service = new HandleResult();
        List<String> resultList = service.readResult();
        log.info("resultList read");
//        service.writeResult(resultList);
        service.pickup(resultList);
        log.info("resultList write end");
    }

    private List<String> readResult() throws Exception {
        File file = new File("d:/read.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<String> resultList = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            if (StringUtils.isBlank(line)) {
                continue;
            }
            resultList.add(String.format("SELECT '%s' hub_code FROM dual UNION ALL", line));
        }
        reader.close();
        return resultList;
    }

    private void writeResult(List<String> resultList) {
        String path = "d:/write.txt";
        for (String result : resultList) {
            FileUtil.append(result + "\n", path, "UTF-8");
        }
    }

    private void pickup(List<String> resultList) {
        int row = 0;
        int count = 1;
        for (String result : resultList) {
            String path = "d:/== pickup/write_" + count + ".txt";
            if (row == 0) {
                FileUtil.append("select t.hub_code from (\n", path, "UTF-8");
            }
            row++;
            if (row >= 200) {
                log.info("resultList write count: " + count);
                FileUtil.append(result.replaceAll("UNION ALL", "") + "\n", path, "UTF-8");
                FileUtil.append(") t where not EXISTS (select hub_code from mst_delivery_hub s where t.hub_code = s.hub_code and s.hub_type = 6)\n", path, "UTF-8");
                row = 0;
                count++;
            } else {
                FileUtil.append(result + "\n", path, "UTF-8");
            }
        }
    }

}


