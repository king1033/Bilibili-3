package org.pqh.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 10295 on 2016/8/4.
 */
public class CrawlerUtil {
    private static Logger log= TestSlf4j.getLogger(CrawlerUtil.class);
    //请求cookie信息
    public static String cookie= "";
    //用户浏览器标识
    private static String userAgent=PropertiesUtil.getProperties("User-Agent",String.class);
    //连接超时时间
    private static int timeout=PropertiesUtil.getProperties("timeout",Integer.class);;
    //发送的表单数据
    public static Map<String,String> formMap=new HashMap<String, String>();
    //代理服务器
    public static List<Proxy> proxyList=new ArrayList<Proxy>();
    /**
     * httpclient get请求封装
     * @param href
     * @return
     */
    public static CloseableHttpResponse doGet(String href){
        log.info("向地址："+href+"发送get请求");
        CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(href);
        httpGet.setHeader("User-Agent", userAgent);
        CloseableHttpResponse closeableHttpResponse= null;
        try {
            closeableHttpResponse = closeableHttpClient.execute(httpGet);
        } catch (IOException e) {
            log.info("get请求发生异常,"+timeout+"秒后重新尝试发送请求");
            ThreadUtil.sleep(log, timeout);
            return doGet(href);
        }
        return closeableHttpResponse;
    }
    /**
     *
     * @param url 爬虫的网址
     * @param tClass 返回的类对象
     * @param method 请求方式
     * @param <T>
     * @return  返回文档信息
     */
    public static <T>T jsoupGet(String url,Class<T> tClass,String method){
        Connection connection=null;
        log.info("连接URL:"+url);
        int i=0;
        try {
            if(tClass== org.dom4j.Document.class){
                SAXReader saxReader=new SAXReader();
                return (T) saxReader.read(url);
            }
            i=(int) (Math.random() * proxyList.size() - 1);

            connection = Jsoup.connect(url).header("Cookie",cookie).userAgent(userAgent).data(formMap).timeout(timeout).ignoreContentType(true);
            if(proxyList.size()>0) {
                connection = connection.proxy(proxyList.get(i));
            }
            if(tClass==Document.class){
                if (method.equals(Constant.GET)) {
                    return (T) connection.get();
                } else if (method.equals(Constant.POST)) {
                    return (T) connection.post();
                } else {
                    throw new RuntimeException("不支持" + method + "请求");
                }
            }
            else if(tClass==String.class){
                return (T) connection.execute().body();
            }else if(tClass==JSONObject.class){
                return (T) JSONObject.fromObject(connection.execute().body());
            }else if(tClass==JSONArray.class){
                return (T) JSONArray.fromObject(connection.execute().body());
            }
            else {
                throw new RuntimeException("返回值不支持"+tClass.getName()+"这种类型");
            }
        }
        catch (IOException e) {
            if(proxyList.size()>0){
                proxyList.remove(i);
            }
            ThreadUtil.sleep(log,10000);
            log.info(e.getMessage());
            return jsoupGet(url,tClass,method);
        } catch (DocumentException e) {
            if(e.getMessage().contains("在文档的元素内容中找到无效的 XML 字符")||e.getMessage().contains("前言中不允许有内容")) {
                log.info(e.getMessage());
                return null;
            }
            log.info("读取xml文档：" + url + "出现异常尝试重新读取");
            ThreadUtil.sleep(log,10000);
            return jsoupGet(url, tClass, method);
        }

    }
    
}
