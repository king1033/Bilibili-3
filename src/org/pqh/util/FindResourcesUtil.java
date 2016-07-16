package org.pqh.util;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.DeflateDecompressingEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;
import org.pqh.entity.BtAcg;
import org.pqh.entity.ComparatorAvPlay;
import org.pqh.test.TaskBtacg;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.zip.DeflaterInputStream;
import java.util.zip.GZIPInputStream;

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
                        e.printStackTrace();
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
        OutputStream fos = null;
        HttpURLConnection connection = null;
        InputStream inputStream=null;
        try {
            URL url = new URL(href);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            String contentEncoding=connection.getContentEncoding();
            String str=null;
            if(contentEncoding.contains("deflate")) {
                HttpClient httpclient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(href);
                org.apache.http.HttpResponse httpResponse=httpclient.execute(httpGet);
                HttpEntity entity = httpResponse.getEntity();
                str= EntityUtils.toString(new DeflateDecompressingEntity(entity));
            }else {
                inputStream = new BufferedInputStream(connection.getInputStream());
            }

            String filename=null;
            if(href.lastIndexOf("=")!=-1){
                filename=href.substring(href.lastIndexOf("=")+1);
            }else if(href.lastIndexOf("/")!=-1){
                filename=href.substring(href.lastIndexOf("/")+1);
            }
            else{
                filename=System.currentTimeMillis()+"";
            }
            File file=new File(outputPath+filename);
            if(contentEncoding.contains("deflate")) {
                FileUtils.writeStringToFile(file,str);
                return;
            }
            fos = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int size=0;
            while ((size = inputStream.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }
        } catch (MalformedURLException e) {
            TestSlf4j.outputLog(e,log);
        } catch (IOException e) {
            TestSlf4j.outputLog(e,log);
        }finally {
            try{
                if(inputStream!=null){
                    inputStream.close();
                }
                if(fos!=null){
                    fos.close();
                }
                if(connection!=null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                TestSlf4j.outputLog(e,log);
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
