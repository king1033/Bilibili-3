package org.pqh.service;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pqh.dao.BiliDao;
import org.pqh.entity.*;
import org.pqh.test.Test;
import org.pqh.util.BiliUtil;
import org.quartz.*;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.swing.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Calendar;

/**
 * Created by 10295 on 2016/5/19.
 */
@Service
public class AvCountService {
    @Resource
    private BiliDao biliDao;

    public Map<String, List> getAvCount() {
        List<AvCount> avCountList = biliDao.selectAvCount();
        List<String> stringList = new ArrayList<String>();
        List<Integer> integerList = new ArrayList<Integer>();
        Map<String, List> map = new HashMap<String, List>();
        for (AvCount avCount : avCountList) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(avCount.getDate());
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DATE);
            stringList.add(year + "年" + month + "月" + day + "日");
            integerList.add(avCount.getCount());
        }
        map.put("Date", stringList);
        map.put("Count", integerList);
        return map;
    }

    public  void setPlays() {

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(new Date());

        try {
            Thread.sleep((int)(Math.random()*7000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(biliDao.selectParam("Avplay").equals("false")){
            return;
        }else{
            biliDao.updateParam(new Param("Avplay","false"));
        }

        int minute=calendar.get(Calendar.MINUTE);
        if(minute!=0&&minute%10!=0){
            return;
        }
        calendar.set(Calendar.SECOND,0);

        Date date=calendar.getTime();
        String datestr=simpleDateFormat.format(date);
        Timestamp timestamp=Timestamp.valueOf(datestr);
        List<String> stringList= new ArrayList<String>();
        List<AvPlay> avPlays=new ArrayList<AvPlay>();
        String json = BiliUtil.jsoupGet("http://www.bilibili.com/api_proxy?app=bangumi&action=get_season_by_tag_v2&tag_id=143&page=1&pagesize=50&indexType=1");
        JSONObject jsonObject = JSONObject.fromObject(json);
        JSONArray jsonArray=jsonObject.getJSONObject("result").getJSONArray("list");
        for (Object object : jsonArray) {
            jsonObject = JSONObject.fromObject(object);
            String season_id = jsonObject.get("season_id").toString();
            String title = jsonObject.get("title").toString();
            Document document = BiliUtil.inputUrl("http://bangumi.bilibili.com/anime/" + season_id);
            Elements elements = document.select(".v1-long-text");
            if (elements.size() == 0) {
//                log.info("动画："+title+"：未开播");
                continue;
            }
            Integer plays=0;
            outterLoop:for (Element element : elements) {
                String href = element.attr("href");
                href = href.substring(href.lastIndexOf("/") + 1);
                json = BiliUtil.jsoupGet("http://bangumi.bilibili.com/web_api/episode/get_source?episode_id=" + href);
                jsonObject = JSONObject.fromObject(json);
                href = "http://api.bilibili.com/view?type=xml&appkey=12737ff7776f1ade&id=" + jsonObject.getJSONObject("result").get("aid").toString();
                String code="";
                int errorCount=0;
                do {
                    document = BiliUtil.inputUrl(href);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    code=document.select("code").html();
                    if(errorCount>3){
                        break outterLoop;
                    }
                    errorCount++;
                }while(code.equals("-503")||code.equals("-403"));
//                log.info("动画："+title+" 单集："+document.title()+"：播放量："+document.select("play").text());
                stringList.add("'"+title+"'");
                plays+=Integer.parseInt(document.select("play").text());
            }
            plays=plays / elements.size();
            AvPlay avPlay=new AvPlay(title,plays,timestamp);
            avPlays.add(avPlay);
        }
        Collections.sort(avPlays,new ComparatorAvPlay());
        int ranking=avPlays.size();
        for(AvPlay avPlay1:avPlays){
            avPlay1.setRanking(ranking--);
        }
        biliDao.insertAvPlay(avPlays);
        biliDao.updateParam(new Param("Avplay","true"));
    }

    public  Map<String,Object> getAvPlay(){
        List<AvPlay> list=biliDao.selectAvPlay();
        List<Integer> play=new ArrayList<Integer>();
        Map<String,Object> map=new HashMap<String, Object>();
        List<Ranking> rankings=biliDao.selectRanking();
        for(AvPlay avPlay:list){
            if(avPlay.getPlay()==0){
                play.add(null);
            }else {
                play.add(avPlay.getPlay());
            }
            if(list.indexOf(avPlay)+1==list.size()){
                map.put(avPlay.getTitle(),play);
                break;
            }
            if(!list.get(list.indexOf(avPlay)+1).getTitle().equals(avPlay.getTitle())){
                    map.put(avPlay.getTitle(),play);
                    play=new ArrayList<Integer>();
            }
//            time.add(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(avPlay.getTimestamp()));

        }
        map.put("Rankings",rankings);
        return map;
    }




}
