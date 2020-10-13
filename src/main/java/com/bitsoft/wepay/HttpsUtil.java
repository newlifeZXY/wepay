package com.bitsoft.wepay;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @Author: jameszhang
 * @Date 2020/8/28 15:48
 */
@Slf4j
public class HttpsUtil {

    private static Integer httpTimeOut  = 2000;
    private HttpsUtil() {
    }

    public static CloseableHttpClient getHttpClient() {
        CloseableHttpClient client = null;
        try {
            client = HttpClientBuilder.create().build();

        } catch (Exception e) {
            log.error("获取httpClient链接发生异常:", e);
        }
        return client;
    }

    public static String doPost(String url, String data){
        CloseableHttpClient httpClient = getHttpClient();
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(httpTimeOut)
            .setConnectTimeout(httpTimeOut)
            .setConnectionRequestTimeout(httpTimeOut)
            .build();
        httpPost.setConfig(requestConfig);
        StringEntity reqEntity = new StringEntity(data, ContentType.create("application/json", "utf-8"));
        httpPost.addHeader("Accept", ContentType.APPLICATION_JSON.toString());
        httpPost.addHeader("Request-ID","");
        httpPost.addHeader("Wechatpay-Nonce","tTuKqXLs8iqmdaCxOZmffn2GjlktxERC");
        httpPost.addHeader("Wechatpay-Timestamp","1602335589");
        httpPost.addHeader("Wechatpay-Serial","46EC6E47AF2C689879756C14C4DC32A0F3E7A32A");
        httpPost.addHeader("Wechatpay-Signature","TVrshicTwJiRIdtlHIBpU9Q5vqmm8+x97e7OUqQdT216NvEdZlTUK5k1xNzMDUMXXIYiDkecL767v1a84+AjsSEUcuqOmwsF7+Wgm3PYyYPTkrZ8ijqCp8f885nksPSbx2r0iwnzAB9msM4wj2ugYonzvP0GfXwfUAYkNUg+uIvgU70Xk8YFQmWtfqFHOkgrGjbtEsZqnmZSZcyg8QX/yRMaAopyn6EMBjA3nY2mIXKYkp7B5LEuuAFraZItVaZbWXoXblWDrSCv7VO2VOUKIrlfovzCfdtFBHY4Jrz2bTfYa2zQxusCxx/G4m52OjqdU4DrKFAThGKju/Nxws4CQw==");
        httpPost.setEntity(reqEntity);
        String content = "";
        try {
            content = httpClient.execute(httpPost, getRespHandler());
        } catch (ConnectTimeoutException e) {
            log.error("系统超时", e);
        } catch (IOException e) {
            log.error("调用微信接口发生异常:", e);
        }
        log.info("微信代金券接口{}返回:{}", url, content);
        return content;
    }

    public static ResponseHandler<String> getRespHandler() {
        return response -> {
            int status = response.getStatusLine().getStatusCode();
            log.info("status:{}", status);
            Header requestId = response.getFirstHeader("Request-ID");
            if (requestId != null) {
                log.info("Request-ID:{}", requestId.getValue());
            }
            String result = null;
            HttpEntity entity = response.getEntity();
            result = entity != null ? EntityUtils.toString(entity) : null;
            return result;
        };
    }

    /**
     * @param url
     * @param param 如果当前url是完整的不需要上送
     * @param token
     * @return
     * @throws
     */
    public static String doGet(String url, Map<String, Object> param, String token) {
        String content = "";
        try {
            CloseableHttpClient httpClient = getHttpClient();
            URIBuilder uriBuilder = new URIBuilder(url);
            if (param != null) {
                for (Entry<String, Object> entry : param.entrySet()) {
                    uriBuilder.addParameter(entry.getKey(), String.valueOf(entry.getValue()));
                }
            }
            log.info("完整请求url:{}", uriBuilder.build());
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(httpTimeOut)
                .setConnectTimeout(httpTimeOut)
                .setConnectionRequestTimeout(httpTimeOut)
                .build();
            httpGet.setConfig(requestConfig);
            httpGet.setHeader("Accept", ContentType.APPLICATION_JSON.toString());
            content = httpClient.execute(httpGet, getRespHandler());
        } catch (ConnectTimeoutException e) {
            log.error("系统超时", e);
        } catch (IOException | URISyntaxException e) {
            log.error("调用微信接口发生异常:", e);
        }
        log.info("微信代金券接口{}返回:{}", url, content);
        return content;
    }

    public static File download(String url, String fileName, String token)  {
        CloseableHttpClient httpClient = getHttpClient();
        log.info("下载url:{},下载文件:{}", url, fileName);
        File wechatDownPath = new File("");
        if (!wechatDownPath.exists()) {
            wechatDownPath.mkdirs();
        }
        HttpGet httpGet = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom()
            .setSocketTimeout(httpTimeOut)
            .setConnectTimeout(httpTimeOut)
            .setConnectionRequestTimeout(httpTimeOut)
            .build();
        httpGet.setConfig(requestConfig);
        httpGet.setHeader("Accept", ContentType.APPLICATION_JSON.toString());
        File localFile = new File(wechatDownPath, fileName);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            log.info("statusCode:{}", statusCode);
            HttpEntity entity = response.getEntity();
            log.info("开始下载文件:{}", fileName);
            FileUtils.copyInputStreamToFile(entity.getContent(), localFile);
            log.info("下载文件完成:{},目标文件:{}", fileName, localFile.getAbsoluteFile());
        } catch (ConnectTimeoutException e) {
            log.error("系统超时", e);
        } catch (ClientProtocolException e) {
            log.error("调用微信接口发生异常:", e);
        } catch (IOException e) {
            log.error("读取信息发生异常:", e);
        }
        return localFile;
    }
}
