package com.wwh.iptools.ip;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * <pre>
 * 获取ip地址
 * </pre>
 *
 * @author wwh
 * @date 2015年9月24日 下午1:26:19
 *
 */
public class IpAddressGetter {

    private static final Logger log = LoggerFactory.getLogger(IpAddressGetter.class);

    public static final String API = "http://pv.sohu.com/cityjson?ie=utf-8";

    private CloseableHttpClient httpclient;

    /**
     * <pre>
     * 构造方法
     * </pre>
     *
     */
    public IpAddressGetter() {
        httpclient = HttpClients.createDefault();
    }

    // 调用其他API

    /**
     * 调用sohu的API
     * 
     * @return
     * @throws Exception
     */
    public IpEntity getIpAddress() throws Exception {
        if (httpclient == null)
            throw new RuntimeException("httpClient 为空了");

        HttpGet httpGet = new HttpGet(API);

        CloseableHttpResponse response = httpclient.execute(httpGet);

        try {

            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
                return null;

            HttpEntity entity = response.getEntity();

            // 读取字符串
            String rpStr = EntityUtils.toString(entity);

            log.debug("API调用结果：{}", rpStr);

            rpStr = rpStr.substring(rpStr.indexOf("{"), rpStr.lastIndexOf("}") + 1);

            JSONObject jsonObj = JSON.parseObject(rpStr);

            log.debug("IP是：{}", jsonObj.get("cip"));

            EntityUtils.consume(entity);

            IpEntity ent = new IpEntity();
            ent.setIpAddress(jsonObj.getString("cip"));
            ent.setLocation(jsonObj.getString("cname"));
            return ent;

        } finally {
            response.close();
        }
    }

    public void close() {
        try {
            httpclient.close();
        } catch (IOException e) {
            log.error("关闭httpclient 异常", e);
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(new IpAddressGetter().getIpAddress());
    }

}
