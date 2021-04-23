//package com.chen.local.scm;
//
//import com.suning.framework.scm.client.SCMClient;
//import com.suning.framework.scm.client.SCMClientFactory;
//import com.suning.framework.scm.client.SCMNode;
//import org.apache.commons.lang3.StringUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.InitializingBean;
//
//import java.util.HashSet;
//import java.util.Set;
//
///**
// * @description SCM配置监听
// * <p>〈功能详细描述〉</p>
// *
// * @auther  陈晨
// * @date    2020/1/4 11:18
// */
//public class SCMListener implements InitializingBean {
//    private Logger logger = LoggerFactory.getLogger(SCMListener.class);
//
//    // aaaa.conf,bbb.conf,ccc.conf
//    private String scmPathes;
//
//    public void setScmPathes(String scmPathes) {
//        this.scmPathes = scmPathes;
//    }
//
//    @Override
//    public void afterPropertiesSet() {
//        this.syncSCMInfo(true);
//        this.runProtectionThread();
//    }
//
//    private SCMClient getSCMClient() {
//        return SCMClientFactory.getSCMClient();
//    }
//
//    private void syncSCMInfo(boolean isMonitor) {
//        for (String path : scmPathes.split(",")) {
//            final String finalPath = "/" + path;
//            SCMClient client = this.getSCMClient();
//            SCMNode scmNode = client.getConfig(finalPath);
//            scmNode.sync();
//            this.process(finalPath, scmNode.getValue());
//            logger.debug("【SCM配置】path={}, value={}, 初始化", finalPath, scmNode.getValue());
//
//            if (isMonitor) {
//                scmNode.monitor(scmNode.getValue(), (oldValue, newValue) -> {
//                    logger.debug("【SCM配置】path={}, oldValue={}, newValue={}, 配置更新"
//                            , finalPath, oldValue, newValue);
//                    this.process(finalPath, newValue);
//                });
//            }
//        }
//    }
//
//    private void process(String path, String value) {
//        if (StringUtils.isBlank(value)) {
//            return;
//        }
//        // set
//        String[] keyValues = value.split("\\r|\\n");
//        Set<String> setKeySet = new HashSet<>();
//        for (String keyValue : keyValues) {
//            if (StringUtils.isBlank(keyValue)) {
//                continue;
//            }
//            String[] kv = keyValue.split("=");
//            setKeySet.add(kv[0]);
//            if (kv[1].equals(SCMCache.get(path, kv[0]))) {
//                continue;
//            }
//            SCMCache.put(path, kv[0], kv[1]);
//        }
//        // remove
//        Set<String> cacheKeySet = SCMCache.getKeySet(path);
//        for (String key : cacheKeySet) {
//            if (!setKeySet.contains(key)) {
//                SCMCache.remove(path, key);
//            }
//        }
//    }
//
//    private void runProtectionThread() {
//        new Thread(() -> {
//            while (true) {
//                try {
//                    this.syncSCMInfo(false);
//                    Thread.sleep(5 * 60 * 1000L);
//                } catch (Exception e) {
//                    logger.error("【SCM配置】守护线程异常, {}", e.getMessage(), e);
//                }
//            }
//        }).start();
//    }
//
//}
//
//
