package org.pqh.util;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pqh.entity.BtAcg;
import org.pqh.entity.ComparatorAvPlay;
import org.pqh.test.TaskBtacg;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.*;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.util.*;

/**
 * Created by 10295 on 2016/7/10.
 */
public class FindResourcesUtil {
    private static Logger log=TestSlf4j.getLogger(FindResourcesUtil.class);
    public static Map<String,List<BtAcg>> map=new HashMap<String, List<BtAcg>>();
    /**
     * 多线程从Btacg搜索关键字资源种子
     * @param threadPoolTaskExecutor
     * @param keyword
     * @return
     */
    public static Map<String,List<BtAcg>> findBy_Btacg(ThreadPoolTaskExecutor threadPoolTaskExecutor,String keyword) {

        int page = 1;
        eachPage(keyword, page);
        if (TaskBtacg.pages > 1) {
            page = 2;
            do {
                TaskBtacg taskBtacg = new TaskBtacg(page, keyword);
                if (!taskBtacg.getFlag()) {
                    threadPoolTaskExecutor.execute(taskBtacg);
                    page++;
                }
            } while (!(threadPoolTaskExecutor.getThreadPoolExecutor().getCompletedTaskCount() == TaskBtacg.pages - 1));
        }
        for (String key : map.keySet()) {
            Collections.sort(map.get(key), new ComparatorAvPlay("size"));
        }
        return map;
    }

    /**
     * 获取单页搜索结果
     * @param keyword 关键字
     * @param page 页数
     */
    public static void eachPage(String keyword,int page){
        Document document = BiliUtil.jsoupGet(Constant.BTACG+ keyword + "&page=" + page, Document.class, Constant.GET);
        if(page==1) {
            String message=document.select(".text_bold").text();
            System.out.println(message);
            int i=Integer.parseInt(BiliUtil.matchStr(message,"\\d+条",String.class).replaceAll("条",""));
            TaskBtacg.pages=i/30+(i%30==0?0:1);
        }
        Elements elements = document.select("#listTable>tbody>tr");
        eachTable(elements, map);
    }

    /**
     * 种子资源筛选
     * @param map 筛选种子
     * @param bt  筛选条件
     * @return
     */
    public static Map<String,String> screenUrl(Map<String,List<BtAcg>> map,BtAcg bt){
        Map<String,String> href=new HashMap<String, String>();
        Field fields[]=bt.getClass().getDeclaredFields();
        for(String type:map.keySet()) {
            List<BtAcg> btAcgs=map.get(type);
            for(BtAcg btAcg:btAcgs) {
                boolean flag=true;
                for (Field field : fields) {
                    field.setAccessible(true);
                    try {
                        if(field.getName().equals("resourceName")&&!field.get(btAcg).toString().contains(field.get(bt).toString())){
                            flag=false;
                            break;
                        }
                        else if(field.get(bt)!=null&&field.get(btAcg).equals(field.get(bt))){
                            flag=false;
                            break;
                        }
                    } catch (IllegalAccessException e) {
                        TestSlf4j.outputLog(e,log);
                    }
                }
                if(flag){
                    href.put(btAcg.getResourceName(),btAcg.getHref());
                }
            }
        }

        return href;
    }

    /**
     * 遍历Btacg每一页的表格信息
     * @param elements
     * @param map
     * @return
     */
    private static Map<String,List<BtAcg>> eachTable(Elements elements, Map<String, List<BtAcg>> map){
        Field[] fields= BtAcg.class.getDeclaredFields();
        for(Element element:elements){
            BtAcg btAcg=new BtAcg();
            int i=0;
            for(Element td:element.select("td")){
                fields[i].setAccessible(true);
                try {
                    if (i != element.select("td").size() - 1) {
                        String text=td.text();
                        if(fields[i].getName().equals("size")) {
                            if(text.replaceAll("[\\d\\.]","").equals("GB")) {
                                text = Double.parseDouble(text.replaceAll("[A-Z]", "")) * 1024 + "";
                            }else{
                                text =text.replaceAll("[A-Z]", "");
                            }
                        }
                        fields[i].set(btAcg,text);
                    } else {
                        fields[i].set(btAcg, "http://bt.acg.gg//"+td.select("a").attr("href"));
                    }
                }catch (IllegalAccessException e) {
                    TestSlf4j.outputLog(e,log);
                }
                i++;
            }
            if(map.get(btAcg.getType())==null){
                List<BtAcg> list=new ArrayList<BtAcg>();
                list.add(btAcg);
                map.put(btAcg.getType(),list);
            }else{
                map.get(btAcg.getType()).add(btAcg);
            }
        }
        return map;
    }

    /**
     * 下载资源
     * @param href 下载链接
     * @param outputPath 输出文件
     */
    public static void downLoad(String href,String outputPath){
        System.out.println("下载链接"+href);
        OutputStream outputStream = null;
        HttpEntity httpEntity=null;
        String filename=null;
        String str=null;
        CloseableHttpResponse closeableHttpResponse=null;
        try {
            closeableHttpResponse=BiliUtil.doGet(href);
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
            File file=new File(outputPath);
            FileUtils.write(file,"UTF-8");
            outputStream=new FileOutputStream(file);
            httpEntity.writeTo(outputStream);
            if(href.contains("comment.bilibili.com")) {
                saveDanmu(outputStream,file);
            }

        } catch (MalformedURLException e) {
            TestSlf4j.outputLog(e,log);
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
                EntityUtils.consume(httpEntity);
            } catch (IOException e) {
                TestSlf4j.outputLog(e,log);
            }
        }

    }

    public static void saveDanmu(OutputStream outputStream,File file){
        SAXReader saxReader=new SAXReader();
        InputStream inputStream=null;
        int count=0;
        int max_count=0;
        int min_count=0;
        try {
            inputStream=new FileInputStream(file);
            org.dom4j.Document document=saxReader.read(inputStream);
            count=saxReader.read(file).getRootElement().elements("d").size();
            max_count=Integer.parseInt(document.getRootElement().element("maxlimit").getStringValue());
            min_count=Integer.parseInt(BiliUtil.getPropertie("danmu%"));
            if(min_count==0){
                min_count=1;
            }else if(Integer.parseInt(BiliUtil.getPropertie("danmu%"))<100){
                min_count=max_count*min_count/100;
            }

        } catch (DocumentException e) {
            TestSlf4j.outputLog(e,log);
        } catch (FileNotFoundException e) {
            TestSlf4j.outputLog(e,log);
        }finally {
            if(count<min_count){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                log.info(file.getName()+"只有"+count+"条弹幕达不到"+min_count+"条最低标准不予保留！！！");
                file.delete();
            }
        }

    }

    /**
     * 文件名替换非法字符
     * @param filename 文件名
     * @return
     */
    public static String switchFileName(String filename){
        return filename.replaceAll("\\\\","").replaceAll("/","").replaceAll(":","").replaceAll("<","").replaceAll(">","").replaceAll("|","");
    }
}
