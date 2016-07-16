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
import org.pqh.util.Constant;
import org.quartz.*;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
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
        Timestamp timestamp=new Timestamp(System.currentTimeMillis());
        List<AvPlay> avPlays=new ArrayList<AvPlay>();
        JSONObject jsonObject = BiliUtil.jsoupGet(Constant.BANGUMIAPI,JSONObject.class, Constant.GET);
        JSONArray jsonArray=jsonObject.getJSONObject("result").getJSONArray("list");
        for (Object object : jsonArray) {
            jsonObject=JSONObject.fromObject(object);
            String bgmid=jsonObject.get("season_id").toString();
            String title=jsonObject.get("title").toString();
            Document document=BiliUtil.jsoupGet(Constant.BGMIDAPI+bgmid+".ver",Document.class,Constant.GET);
            String jsonStr=document.body().html();
            jsonStr=jsonStr.substring(jsonStr.indexOf("{"),jsonStr.lastIndexOf("}"))+"}";
            jsonObject=JSONObject.fromObject(jsonStr).getJSONObject("result");
            int newest_ep_index=jsonObject.getInt("newest_ep_index");
            int avgPlay=jsonObject.getInt("play_count")/newest_ep_index;
            AvPlay avPlay=new AvPlay(title,avgPlay,timestamp);
            avPlays.add(avPlay);
        }
        Collections.sort(avPlays,new ComparatorAvPlay("play"));
        int ranking=avPlays.size();
        for(AvPlay avPlay1:avPlays){
            avPlay1.setRanking(ranking--);
        }
        biliDao.insertAvPlay(avPlays);
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
