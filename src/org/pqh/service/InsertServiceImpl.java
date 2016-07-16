package org.pqh.service;

import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.pqh.dao.BiliDao;
import org.pqh.dao.VstorageDao;
import org.pqh.entity.Bangumi;
import org.pqh.entity.Bili;
import org.pqh.test.Test;
import org.pqh.util.BiliUtil;
import org.pqh.util.Constant;
import org.pqh.util.TestSlf4j;
import org.quartz.CronTrigger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class InsertServiceImpl implements InsertService {
	private static Logger log= TestSlf4j.getLogger(InsertServiceImpl.class);
	@Resource
	private BiliDao biliDao;
	@Resource
	private VstorageDao vstorageDao;
	@Resource
	private CronTrigger cronTrigger;

	public CronTrigger getCronTrigger() {
		return cronTrigger;
	}

	public Integer getTimer(){
		return Integer.parseInt(cronTrigger.getCronExpression().split(" ")[2].split("/")[1]);
	}

	public static int count;


	@Override
	public void insertBili(int aid,int page) {
		Map<String, Object> map;
		Bili bili=null;
		Bangumi bangumi=null;

		while(true){
			do{
				List list=BiliUtil.setView(aid,page);
				if(list==null){
					break;
				}
				bili= (Bili) list.get(0);
				bangumi= (Bangumi) list.get(1);
				bili.setAid(aid);
				bili.setTypename2(BiliUtil.getBq(bili.getTypename()));
				bili.setPartid(page);
				try{
					if(page==1){
						if(bangumi.getBangumi_id()!=null){
							biliDao.insertBangumi(bangumi);
						}
						biliDao.insertBili(bili);
					}
					biliDao.insertCid(bili);
				}
				catch(DuplicateKeyException e){
					if(e.getMessage().contains("insertBili")){
						biliDao.updateBili(bili);
					}else if(e.getMessage().contains("insertBangumi")){
						biliDao.updateBangumi(bangumi);
					}
					else{
						biliDao.updateCid(bili);
					}
				}
				page++;
			}while(page<=bili.getPages());
			biliDao.setAid(aid,1);
			aid++;
			page=1;
		}
	}

	public void insertVstorage(int cid){
		Map<String, Object> map = new HashMap<String, Object>();
		Test test =new Test();
		String classnames[] = Constant.CLASSNAME.split(",");
		for (String classname : classnames) {
			try {
				if (classname.contains("<")) {
					StringBuffer stringBuffer = new StringBuffer(classname);
					String type1 = stringBuffer.substring(stringBuffer.indexOf("<") + 1, stringBuffer.indexOf(">"));
					String type2 = stringBuffer.substring(0, stringBuffer.indexOf("<"));
					map.put(type1, test.getClass(type2));
				} else {
					Class c = Class.forName(classname);
					map.put(c.getName(), c.newInstance());
				}
			} catch (ClassNotFoundException e) {
				TestSlf4j.outputLog(e,log);
			} catch (InstantiationException e) {
				TestSlf4j.outputLog(e,log);
			} catch (IllegalAccessException e) {
				TestSlf4j.outputLog(e,log);
			}
		}
		String classname = null;
		try {
			classname = Class.forName(classnames[0]).getName();
		} catch (ClassNotFoundException e) {
			TestSlf4j.outputLog(e,log);
		}

		String url = Constant.VSTORAGEAPI + cid;
		JSONObject jsonObject = BiliUtil.jsoupGet(url,JSONObject.class,Constant.GET);
		if(jsonObject.get("list")!=null){
			count++;
			return;
		}

		map = test.getMap(jsonObject, map, classname, false, 0, cid);
		test.setData(vstorageDao, map);
		biliDao.setAid(cid, 3);

	}

	public  void insertCid(int cid){
		String url = Constant.CIDAPI+cid;
		String data= null;
		data = BiliUtil.jsoupGet(url, Document.class,Constant.GET).toString().trim();
		Map<String,Object> map=BiliUtil.getdata(Constant.CIDLIST,data);
		map.put("cid",cid);
		String aid =map.get("aid").toString();
		if(aid.equals("-1")||aid.equals("1")){
			count++;
			return;
		}
		String oriurl=map.get("oriurl").toString();
		String vtype=map.get("vtype").toString();
		if(oriurl.contains("<")) {
			if(oriurl.contains("vid")) {
				String o = oriurl.substring(0, oriurl.indexOf("vid=") + "vid=".length());
				try {
					int vid = Integer.parseInt(BiliUtil.matchStr(oriurl, ">\\d+<",String.class).replaceAll("\\D", ""));
					o+= vid;
					map.put("oriurl", o);
				} catch (NumberFormatException e) {
					map.put("oriurl",o+="?");
				}
			}else if(vtype.equals("youku")){
				String regexs[]={">\\w+<",">\\w+","\\w+\\s+<"};
				String str1= BiliUtil.matchStr(oriurl,regexs,"\\W+");
				if(!str1.equals("")){
					String o="http://v.youku.com/v_show/id_"+str1.replaceAll("id_","");
					map.put("oriurl",o);
				}
			}
		}

		try {
			biliDao.insertC(map);
		}catch (DuplicateKeyException e){
			biliDao.updateC(map);
		}
		biliDao.setAid(cid,2);
	}
	@Override
	public void insertLimit(Integer aid) {
		if(biliDao.count(aid)==20){
			aid=biliDao.getLimit(aid);
			System.out.println(aid);
			biliDao.setLimit(aid);
			insertLimit(aid);
		}else{
			return;
		}
	}


}
