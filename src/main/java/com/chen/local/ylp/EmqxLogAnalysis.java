package com.chen.local.ylp;

import com.alibaba.fastjson.JSONObject;
import com.chen.local.base.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @description SQL输出
 * <p> <功能详细描述> </p>
 *
 * @author 陈晨
 * @date 2024/8/20 18:31
 */
@Slf4j
public class EmqxLogAnalysis {
    private static final Pattern PEERNAME_PATTERN =
            Pattern.compile("peername: (\\d+\\.\\d+\\.\\d+\\.\\d+:\\d+)");
    private static final Pattern CLIENTID_PATTERN =
            Pattern.compile("clientid: (\\d{32})");

    // 匹配日志中的topic字段
    private static final Pattern TOPIC_PATTERN =
            Pattern.compile("topic: ([^,]+)");
    // 匹配我们关心的特定主题模式
    private static final Pattern TARGET_TOPIC_PATTERN =
            Pattern.compile("mqtt/device2Server/[^/]+/(\\d{4})");
    // 匹配特定主题模式：mqtt/device2Server/{clientId}/3100
    private static final Pattern CLIENT_ID_PATTERN =
            Pattern.compile("mqtt/device2Server/([^/]+)/(\\d{4})");

    private static final Pattern TIME_PATTERN = Pattern.compile(
            "\\[(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\.\\d{3})\\]"
    );

    public static void main(String[] args) throws Exception {
        // lastOutboundActivity=1751532675131
        // lastInboundActivity=1751532625691
        // time=1751532676052
        // lastPing=1751532651052
        System.out.println("lastOutboundActivity: " + DateUtil.format(new Date(1751532675131L), DateUtil.DATE_ALL));
        System.out.println("lastInboundActivity: " + DateUtil.format(new Date(1751532625691L), DateUtil.DATE_ALL));
        System.out.println("time: " + DateUtil.format(new Date(1751532676052L), DateUtil.DATE_ALL));
        System.out.println("lastPing: " + DateUtil.format(new Date(1751532651052L), DateUtil.DATE_ALL));
    }

    private static void readFile() throws Exception {
        Map<String, Long> counterMap = new HashMap<>();
//        Map<String, Integer> clientidCounts = new HashMap<>();

//        File file = new File("/Users/chenchen/Downloads/mqtt问题排查/mqtt-provincial-connector-bak-1.log");
        File file = new File("/Users/chenchen/Downloads/mqtt问题排查/mqtt-provincial-connector-bak 2.log");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        Date lastTime = null;
        while ((line = reader.readLine()) != null) {
            if (StringUtils.isBlank(line)) {
                continue;
            }
//            extractAndCount(line, PEERNAME_PATTERN, counterMap);
//            extractAndCount(line, CLIENTID_PATTERN, clientidCounts);
            Date currTime = extractTimestamp(line, lastTime, counterMap);
            if (null != currTime) {
                lastTime = currTime;
            }
        }
        reader.close();
        System.out.println(counterMap.size());

        Map<String, Long> sortMap = sortByValueDescending(counterMap);
        int count = 0;
        for (Map.Entry<String, Long> entry : sortMap.entrySet()) {
            System.out.println(entry);
            if (++count >= 100) {
                break;
            }
        }

//        System.out.println(clientidCounts.size());
//        System.out.println(sortByValueDescending(clientidCounts));
    }

    public static Date extractTimestamp(String line, Date lastTime, Map<String, Long> counterMap) {
        Matcher matcher = TIME_PATTERN.matcher(line);
        if (!matcher.find()) {
            return null;
        }
        String timeStr = matcher.group(1);
        Date time = DateUtil.parse(timeStr, DateUtil.DATE_ALL);
        if (null == time) {
            return null;
        }
        if (null == lastTime) {
            counterMap.put(timeStr, 0L);
        } else {
//            System.out.println(time.getTime() + " - " + lastTime.getTime() + " = " + (time.getTime() - lastTime.getTime()));
            counterMap.put(timeStr, time.getTime() - lastTime.getTime());
        }
        return time;
    }

    private static void extractAndCount(String line, Pattern pattern, Map<String, Integer> counterMap) {
//        Matcher matcher = pattern.matcher(line);
//        if (matcher.find()) {
//            String key = matcher.group(1);
//            counterMap.put(key, counterMap.getOrDefault(key, 0) + 1);
//        }

        Matcher topicMatcher = TOPIC_PATTERN.matcher(line);
        if (topicMatcher.find()) {
            String topic = topicMatcher.group(1);
            Matcher targetMatcher = CLIENT_ID_PATTERN.matcher(topic);
            if (targetMatcher.find()) {
                String suffix = targetMatcher.group(1);
                counterMap.put(suffix, counterMap.getOrDefault(suffix, 0) + 1);
            }
        }
    }

    private static Map<String, Long> sortByValueDescending(Map<String, Long> map) {
        return map.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

}


