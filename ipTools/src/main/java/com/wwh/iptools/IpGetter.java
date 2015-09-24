package com.wwh.iptools;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * <pre>
 * 
 * </pre>
 *
 * @author wwh
 * @date 2015年9月24日 下午1:26:19
 *
 */
public class IpGetter {

    public static String API = "http://pv.sohu.com/cityjson?ie=utf-8";

    public static void main(String[] args) throws Exception, IOException {

        CloseableHttpClient httpclient = HttpClients.createDefault();

        try {
            HttpGet httpGet = new HttpGet(API);
            CloseableHttpResponse response1 = httpclient.execute(httpGet);
            try {
                System.out.println(response1.getStatusLine());
                HttpEntity entity1 = response1.getEntity();

                // 读取字符串
                String rpString = EntityUtils.toString(entity1);
                System.out.println(rpString);

                EntityUtils.consume(entity1);
            } finally {
                response1.close();
            }
        } finally {
            httpclient.close();
        }
    }

}
