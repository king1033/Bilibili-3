package org.pqh.util;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.select.Evaluator;
import org.pqh.entity.BtAcg;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 10295 on 2016/7/10.
 */
public class FindResourcesUtil {
    private static Logger log=TestSlf4j.getLogger(FindResourcesUtil.class);
    public static Map<String,List<BtAcg>> findBy_Btacg(String keyword){
        Map<String,List<BtAcg>> map=new HashMap<String, List<BtAcg>>();
        int countPage=1;
        boolean flag=true;
        for(int page=1;page<=countPage;page++) {
            Document document = BiliUtil.jsoupGet("http://bt.acg.gg/search.php?keyword=" + keyword + "&page=" + page, Document.class, BiliUtil.GET);
            if(flag) {
                countPage = Integer.parseInt(document.select(".pager-last").html());
                flag=false;
            }
            Elements elements = document.select("#listTable>tbody>tr");
            map = eachTable(elements, map);
        }
        return map;
    }

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

    public static void downLoadTorrent(String href,String outputPath){
        System.out.println("下载链接"+href);
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(href);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            bis = new BufferedInputStream(connection.getInputStream());
            String type=HttpURLConnection.guessContentTypeFromStream(bis);
            File file=new File(outputPath);
            String filename=href.substring(href.lastIndexOf("/"))==""?href.substring(href.lastIndexOf("=")):"";
            if(file.exists()){
                if(file.isDirectory()){
                    filename+=(type!=null?"."+type:"");
                }
            }else{
                throw  new RuntimeException("不存在这个文件或目录");
            }
            fos = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int size=0;
            while ((size = bis.read(buf)) != -1) {
                fos.write(buf, 0, size);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try{
                if(bis!=null){
                    bis.close();
                }
                if(fos!=null){
                    fos.close();
                }
                if(connection!=null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static String switchFileName(String filename){
        return filename.replaceAll("\\\\","").replaceAll("/","").replaceAll(":","").replaceAll("<","").replaceAll(">","").replaceAll("|","");
    }
}
