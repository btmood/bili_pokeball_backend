package com.lark.spider.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.conn.HttpHostConnectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.net.SocketException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
/**
 * 页面下载工具类
 */
@Component
@Slf4j
public class HttpClientTool {
//    private static final CloseableHttpClient httpClient;
    public static final String CHARSET = "UTF-8";
    private static RedisTemplate redisTemplate = ApplicationContextProvider.getBean("redisTemplate", RedisTemplate.class);
    // 采用静态代码块，初始化超时时间配置，再根据配置生成默认httpClient对象
//    static {
//        RequestConfig config = HttpClientTool.getRequestConfig();
//        httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
//    }



    public static String doGet(String url, Map<String, String> params) {
        return doGet(url, params, CHARSET);
    }

    public static String doGetSSL(String url, Map<String, String> params) {
        return doGetSSL(url, params, CHARSET);
    }

    public static String doPost(String url, Map<String, String> params) throws IOException {
        return doPost(url, params, CHARSET);
    }

    /**
     * HTTP Get 获取内容
     * @param url 请求的url地址 ?之前的地址
     * @param params 请求的参数
     * @param charset 编码格式
     * @return 页面内容
     */
    public static String doGet(String url, Map<String, String> params, String charset) {
        if (StringUtils.isEmpty(url)) {
            System.out.println("url为空？");
            return null;
        }
        System.out.println("111");
        //从key为proxy的redis的set集合中随机取一个ip:port
        String ip_port = (String) redisTemplate.opsForSet().randomMember("proxy");
        RequestConfig config = HttpClientTool.getRequestConfig(ip_port);
        //创建httpClient对象
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
//        try {
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), value));
                    }
                }
                // 将请求参数和url进行拼接
                try {
                    url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            HttpGet httpGet = new HttpGet(url);
            Map<String, String> headers = AntiSpider.getHeaders();
            httpGet.setHeader("User-Agent", headers.get("User-Agent"));
            httpGet.setHeader("Accept", headers.get("Accept"));
            httpGet.setHeader("Accept-Language", headers.get("Accept-Language"));
            httpGet.setHeader("Connection", headers.get("Connection"));
            httpGet.setHeader("Accept-Encoding", headers.get("Accept-Encoding"));
            httpGet.setHeader("Connection","keep-alive");
            httpGet.setHeader("Host","api.bilibili.com");
            httpGet.setHeader("Pragma","no-cache");
            httpGet.setHeader("TE","Trailers");
            httpGet.setHeader("Upgrade-Insecure-Requests","1");
            System.out.println(httpGet);
            //发起请求
        CloseableHttpResponse response = null;
        try {
                response = httpClient.execute(httpGet);
        } catch (SocketException e) {
            //如果当前ip不可用就直接从redis仓库里删除
            redisTemplate.opsForSet().remove("proxy", ip_port);
            //相关url重新进入待爬取队列
            //TODO
            log.error("ip代理无法连接，ip是{}，错误信息是{}",ip_port,e);
            return null;
        }catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(response);
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println(statusCode);
            if (statusCode != 200) {
                httpGet.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                try {
                    result = EntityUtils.toString(entity, "utf-8");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        try {
            EntityUtils.consume(entity);
            response.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * HTTP Post 获取内容
     * @param url 请求的url地址 ?之前的地址
     * @param params 请求的参数
     * @param charset 编码格式
     * @return 页面内容
     * @throws IOException
     */
    public static String doPost(String url, Map<String, String> params, String charset)
            throws IOException {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        //从key为proxy的redis的set集合中随机取一个ip:port
        String ip_port = (String) redisTemplate.opsForSet().randomMember("proxy");
        RequestConfig config = HttpClientTool.getRequestConfig(ip_port);
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        List<NameValuePair> pairs = null;
        if (params != null && !params.isEmpty()) {
            pairs = new ArrayList<NameValuePair>(params.size());
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String value = entry.getValue();
                if (value != null) {
                    pairs.add(new BasicNameValuePair(entry.getKey(), value));
                }
            }
        }
        HttpPost httpPost = new HttpPost(url);
        Map<String, String> headers = AntiSpider.getHeaders();
        httpPost.setHeader("User-Agent", headers.get("User-Agent"));
        httpPost.setHeader("Accept", headers.get("Accept"));
        httpPost.setHeader("Accept-Language", headers.get("Accept-Language"));
        httpPost.setHeader("Connection", headers.get("Connection"));
        httpPost.setHeader("Accept-Encoding", headers.get("Accept-Encoding"));
        if (pairs != null && pairs.size() > 0) {
            httpPost.setEntity(new UrlEncodedFormEntity(pairs, CHARSET));
        }
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpPost.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            return result;
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (response != null)
                response.close();
        }
        return null;
    }

    /**
     * HTTPS Get 获取内容
     * @param url 请求的url地址 ?之前的地址
     * @param params 请求的参数
     * @param charset  编码格式
     * @return 页面内容
     */
    public static String doGetSSL(String url, Map<String, String> params, String charset) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        String ip_port = (String) redisTemplate.opsForSet().randomMember("proxy");
        RequestConfig config = HttpClientTool.getRequestConfig(ip_port);
        CloseableHttpClient httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
        try {
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> pairs = new ArrayList<NameValuePair>(params.size());
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    String value = entry.getValue();
                    if (value != null) {
                        pairs.add(new BasicNameValuePair(entry.getKey(), value));
                    }
                }
                url += "?" + EntityUtils.toString(new UrlEncodedFormEntity(pairs, charset));
            }
            HttpGet httpGet = new HttpGet(url);
            Map<String, String> headers = AntiSpider.getHeaders();
            httpGet.setHeader("User-Agent", headers.get("User-Agent"));
            httpGet.setHeader("Accept", headers.get("Accept"));
            httpGet.setHeader("Accept-Language", headers.get("Accept-Language"));
            httpGet.setHeader("Connection", headers.get("Connection"));
            httpGet.setHeader("Accept-Encoding", headers.get("Accept-Encoding"));

            // https  注意这里获取https内容，使用了忽略证书的方式，当然还有其他的方式来获取https内容
            CloseableHttpClient httpsClient = HttpClientTool.createSSLClientDefault();
            CloseableHttpResponse response = httpsClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpGet.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }
            HttpEntity entity = response.getEntity();
            String result = null;
            if (entity != null) {
                result = EntityUtils.toString(entity, "utf-8");
            }
            EntityUtils.consume(entity);
            response.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 这里创建了忽略整数验证的CloseableHttpClient对象
     * @return
     */
    public static CloseableHttpClient createSSLClientDefault() {
        //获取配置对象
        String ip_port = (String) redisTemplate.opsForSet().randomMember("proxy");
        RequestConfig config = HttpClientTool.getRequestConfig(ip_port);
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                // 信任所有
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
            //返回HttpClient对象
            return HttpClients.custom().setSSLSocketFactory(sslsf).setDefaultRequestConfig(config).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        return HttpClients.createDefault();
    }

    /**
     * 获取RequestConfig对象，配置HttpClient
     * @param ip_port 从key为proxy的redis的set集合中随机取一个ip:port
     * @return
     */
    public static RequestConfig getRequestConfig(String ip_port) {
        RequestConfig config;
        if (!StringUtils.isEmpty(ip_port)) {
            String[] arr = ip_port.split(":");
            String proxy_ip = arr[0];
            int proxy_port = Integer.parseInt(arr[1]);
            //创建代理类
            HttpHost proxy = new HttpHost(proxy_ip,proxy_port);
            config = RequestConfig.custom()
                    .setProxy(proxy)
                    .setConnectTimeout(60000)
                    .setSocketTimeout(15000)
                    .setConnectionRequestTimeout(6000)
                    .build();
        }else {
            //如果没有从redis仓库获取到代理ip，就
            config = RequestConfig.custom()
                    .setConnectTimeout(60000)
                    .setSocketTimeout(15000)
                    .setConnectionRequestTimeout(6000)
                    .build();
        }
        return config;
    }
}
