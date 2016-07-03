package org.pqh.service;

import net.sf.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.pqh.dao.BiliDao;
import org.pqh.entity.AvCount;
import org.pqh.entity.AvPlay;
import org.pqh.util.BiliUtil;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

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
        List<String> stringList=null;
        List<Integer> integerList=null;
        String json = BiliUtil.jsoupGet("http://www.bilibili.com/api_proxy?app=bangumi&action=get_season_by_tag_v2&tag_id=143&page=1&pagesize=50&indexType=1");
        JSONObject jsonObject = JSONObject.fromObject(json);
        for (Object object : jsonObject.getJSONObject("result").getJSONArray("list")) {
            stringList = new ArrayList<String>();
            integerList = new ArrayList<Integer>();
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
            for (Element element : elements) {
                String href = element.attr("href");
                href = href.substring(href.lastIndexOf("/") + 1);
                json = BiliUtil.jsoupGet("http://bangumi.bilibili.com/web_api/episode/get_source?episode_id=" + href);
                jsonObject = JSONObject.fromObject(json);
                href = "http://api.bilibili.com/view?type=xml&appkey=12737ff7776f1ade&id=" + jsonObject.getJSONObject("result").get("aid").toString();
                document = BiliUtil.inputUrl(href);
//                log.info("动画："+title+" 单集："+document.title()+"：播放量："+document.select("play").text());
                stringList.add("'"+title+"'");
                plays+=Integer.parseInt(document.select("play").text());
            }
            plays=plays / elements.size();

            AvPlay avPlay=new AvPlay(title,plays,new Timestamp(System.currentTimeMillis()));
            try {
                biliDao.insertAvPlay(avPlay);
            }catch (DuplicateKeyException e){
                biliDao.updateAvPlay(avPlay);
            }
            System.out.println(avPlay);
        }

    }

    public  Map<String,Object> getAvPlay(){
        List<AvPlay> list=biliDao.selectAvPlay();
        List<Integer> play=new ArrayList<Integer>();
        Set<String> time=new TreeSet<String>();
        Map<String,Object> map=new HashMap<String, Object>();
        int count=0;
        for(AvPlay avPlay:list){
            play.add(avPlay.getPlay());
            count++;
            if(count%biliDao.getAvPlayC()==0){
                map.put(avPlay.getTitle(),play);
                count=0;
                play=new ArrayList<Integer>();
            }
            time.add(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(avPlay.getTimestamp()));
        }
        map.put("time",time);
        return map;
    }




}
