package org.pqh.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jsoup.nodes.Document;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by 10295 on 2016/8/7.
 */
public class DownLoadUtil {
    private static Logger log= TestSlf4j.getLogger(DownLoadUtil.class);
    /**
     * 下载资源
     * @param href 下载链接
     * @param outputPath 输出文件
     */
    public static void downLoad(String href,String outputPath){
        log.info("下载链接:"+href);
        OutputStream outputStream = null;
        HttpEntity httpEntity=null;
        CloseableHttpResponse closeableHttpResponse=null;
        File file=null;
        try {
            closeableHttpResponse=CrawlerUtil.doGet(href);
            httpEntity=closeableHttpResponse.getEntity();
            Class c=httpEntity.getClass().getSuperclass();
            Field field=c.getDeclaredField("wrappedEntity");
            field.setAccessible(true);
            String values=field.get(httpEntity).toString();
            values=values.substring(values.indexOf("[")+1,values.indexOf("]"));
            Map<String,String> map=new HashMap<String, String>();
            for(String value:values.split(",")){
                map.put(value.split(":")[0],value.split(":")[1].trim());
            }
            file=new File(outputPath);
            FileUtils.write(file,"UTF-8");
            outputStream=new FileOutputStream(file);
            httpEntity.writeTo(outputStream);

        } catch (IOException e) {
            if(e.toString().contains("Connection timed out")){
                downLoad(href,outputPath);
            }
        } catch (NoSuchFieldException e) {
            TestSlf4j.outputLog(e,log);
        } catch (IllegalAccessException e) {
            TestSlf4j.outputLog(e,log);
        } finally {
            try {
                outputStream.close();
                EntityUtils.consume(httpEntity);
            } catch (IOException e) {
                TestSlf4j.outputLog(e,log);
            }
        }

    }

    /**
     *  获取弹幕
     * @param href
     * @return
     */
    public static <T>T downLoadDanmu(String href){
        org.dom4j.Document document=null;
        SAXReader saxReader=new SAXReader();
        InputStream in= null;
        OutputStream out=null;
        HttpEntity httpEntity=null;
        File file=null;
        try {
            httpEntity = CrawlerUtil.doGet(href).getEntity();
            if(href.contains("rolldate")) {
                file = new File("时间戳.xml");
                FileUtils.write(file, "UTF-8");
                out=new FileOutputStream(file);
                httpEntity.writeTo(out);
                List<String> list=FileUtils.readLines(file);
                if(list.size()==0){
                    log.error("无法从弹幕编号"+href.replaceAll("\\D","")+"获取到历史弹幕参数");
                    return null;
                }
                return (T) JSONArray.fromObject(list).getJSONArray(0);
            }
            else {
                in = httpEntity.getContent();
                return (T) saxReader.read(in).getRootElement();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }finally {
            try {
                if(out!=null){
                    out.close();
                }
                EntityUtils.consume(httpEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(file!=null) {
                file.delete();
            }

        }
        return null;
    }

    /**
     * 下载历史弹幕
     * @param cid 弹幕编号
     * @param path 保存路径
     */
    public static void downLoadDanmu(int cid,String path){
        int max_count=0;
        int min_count=0;
        Set<String> set=new HashSet<String>();
        String href=Constant.DANMU+cid+".xml";
        org.dom4j.Element element=DownLoadUtil.downLoadDanmu(href);
        for(Element d:element.elements("d")){
            set.add(d.asXML());
        }
        max_count=Integer.parseInt(element.element("maxlimit").getStringValue());
        min_count=PropertiesUtil.getProperties("danmu%",Integer.class);
        if(set.size()<max_count*min_count/100) {
            JSONArray jsonArray = downLoadDanmu(Constant.DANMU + "rolldate," + cid);
            if (jsonArray != null) {
                outer:for (Object object : jsonArray) {
                    Long timestamp = JSONObject.fromObject(object).getLong("timestamp");
                    href = Constant.DANMU + "dmroll," + timestamp + "," + cid;
                    log.info("开始获取" + TimeUtil.formatDateToString(new Date(timestamp * 1000), null) + "历史弹幕");
                    element = downLoadDanmu(href);
                    for (Element d : element.elements("d")) {
                        set.add(d.asXML());
                        if(set.size()==max_count*min_count/100){
                            break outer;
                        }
                    }
                }
            }
        }
        while (element.remove(element.element("d")));
        List<String> list=new ArrayList<String>();
        list.add("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        list.add("<i>");
        for(org.dom4j.Element head:element.elements()){
            list.add(head.asXML());
        }
        list.addAll(set);
        list.add("</i>");
        log.info("弹幕厚度:"+set.size()+"条");
        try {
            FileUtils.writeLines(new File(path),"UTF-8",list);
        } catch (IOException e) {
            TestSlf4j.outputLog(e,log);
        }

    }

    /**
     * 生成闪光文字图片
     * @param text 文字内容
     * @param path 生成路径
     */
    public static  String dLWordArt(String text, final String path, String cookie){
        CrawlerUtil.cookie=cookie;
        Document document= null;
        try {
            document = CrawlerUtil.jsoupGet(Constant.WORDART+ URLEncoder.encode(text,"GBK"),Document.class, Constant.GET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(document.baseUri().contains(Constant.WORDART)&&!document.body().html().contains(".jpg")){
            log.info("assqqlcookie:"+CrawlerUtil.cookie+"已过期");
            cookie=CrawlerUtil.doGet(Constant.ASSQQL).getHeaders("Set-Cookie")[0].getValue();
            PropertiesUtil.updateProperties("assqqlcookie",cookie,null);
            log.info("assqqlcookie:"+cookie+"更新完毕尝试重新发送请求");
            return dLWordArt(text,path,cookie);
        }
        String href=Constant.WORDARTPATH+document.body().html();
        text=FindResourcesUtil.switchFileName(text);
        final String jpgPath=path+text+".jpg";
        downLoad(href,jpgPath);
//        BiliUtil.openImage(new File(path+"/"+text+".jpg"));
        System.gc();
        return jpgPath;
    }

    /**
     * 下载二维码
     * @param text 文字内容
     * @param filepath 生成路径
     * @*param width二维码宽度
     */
    public static void dLQrcode(String text,String filepath,String width){
        downLoad(Constant.QRCODE+text+"&w="+width,filepath);
        BiliUtil.openImage(new File(filepath));
    }
}
