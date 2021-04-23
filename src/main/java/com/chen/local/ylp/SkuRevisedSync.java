package com.chen.local.ylp;

import com.chen.local.base.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品订正
 * <p>〈功能详细描述〉</p>
 *
 * @author 陈晨
 * @version 1.0
 * @date 2020/11/26
 */
@Slf4j
public class SkuRevisedSync {

    public static void main(String[] args) throws Exception {
        SkuRevisedSync service = new SkuRevisedSync();
        List<String> codeList = service.getPlaceCodeList();
        log.info("codeList read");
        for (String code : codeList) {
            try {
                String result = service.sendRequest(code);
                if (result != null) {
                    service.writeResult(result);
                }
                log.info("code={}, result={}, sync complete", code, result);
                Thread.sleep((long) (Math.random() * 30) + 30);
            } catch (Exception e) {
                log.error("code={}, sync fail, {}", code, e.getMessage(), e);
            }
        }
        log.info("sync end");
    }

    private HttpClient client;
    private String sendRequest(String code) throws Exception {
        String merchantCode = code.split(",")[1];
        String skuCode = code.split(",")[0];
        String url = "http://pre-api-ylp.ypsx-internal.com/test/skuRevised/syncPackageInfo/@merchantCode@/@skuCode@?barcode=";
        url = url.replaceFirst("@merchantCode@", merchantCode).replaceFirst("@skuCode@", skuCode);
        log.info("url={}", url);
        if (client == null) {
            client = HttpClientBuilder.create().build();
        }
        HttpResponse resp = client.execute(new HttpGet(url));
        String result = EntityUtils.toString(resp.getEntity());
        if (result.contains(merchantCode) || result.contains(skuCode)) {
            return result;
        }
        return null;
    }

    private List<String> getPlaceCodeList() throws Exception {
        File file = new File("d:/pickupSiteCodes.txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<String> codeList = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            codeList.add(line);
        }
        reader.close();
        return codeList;
    }

    private void writeResult(String result) {
        String path = "d:/pickupSite_SyncResults.txt";
        FileUtil.append(result + "\n", path, "UTF-8");
    }

}


