package org.pqh.util;

import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.pqh.entity.Bangumi;
import org.pqh.entity.Bili;
import org.pqh.entity.Data;
import org.pqh.entity.Type;
import org.pqh.test.Test;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BiliUtil {
	static String cookie=getProperties("BILICOOKIE");
	static boolean flag=false;
	public static final String AIDLIST[]=new String[]{"tid","typename","play","review","video_review","favorites","title","allow_bp","allow_feed","allow_download","description","tag","pic","author","mid","face","pages","instant_server","created","created_at","credit","coins","spid","src","cid","partname","offsite"};
	public static final  String CIDLIST[]=new String[]{"maxlimit","chatid","server","vtype","oriurl","aid","typeid","pid","click","favourites","credits","coins","fw_click","duration","arctype","danmu","bottom","sinapi","acceptguest","acceptaccel"};
	private static Logger log=TestSlf4j.getLogger(BiliUtil.class);
	public static Map<String, Object> getdata(String[] list,String data){
		Map<String, Object> map=new HashMap<String, Object>();
		for(String bq:list){
			int bqide=data.indexOf("<"+bq+">");
			int bqendide=data.indexOf("</"+bq+">");
			int a=data.indexOf("<"+bq);
			int b=data.indexOf("/>");
			String S=null;
			if(bqide!=-1){
				S=data.substring(bqide+bq.length()+2, bqendide).trim();
				if(bq=="code"||bq=="error"){
					map.put(bq, S);
					if(bq=="error"){
						return map;
					}
				}
				else if(S.equals("--")||S.length()==0){
					S="-1";
					}
				
				}
			else if(a!=-1)
				S=data.substring(a+bq.length()+2,b).trim();
			else{
				S="-1";	
			}

			map.put(bq, S);
		}
		return map;
		
	}

	public static List setElement(List<Element> elements,List list,int index){
		Test test=new Test();
		for(org.dom4j.Element element:elements){
			if(element.elements().size()>0){
				setElement(element.elements(),list,1);
			}else{
				test.setObject(list.get(index),element.getName(),element.getText());
			}

		}
		return list;
	}

	public static List setView(int aid,int page){
		SAXReader saxReader=new SAXReader();
		org.dom4j.Document document= null;
		try {
			document = saxReader.read("http://api.bilibili.com/view?type=xml&appkey=12737ff7776f1ade&id="+aid+"&page="+page);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		List list=new ArrayList();
		list.add(new Bili());
		list.add(new Bangumi());
		Element element=document.getRootElement();
		Element code=element.element("code");
		if(code!=null) {
			if (code.getText().equals("-403") || code.getText().equals("-404")) {
				return null;
			} else if (code.getText().equals("-503")) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return setView(aid, page);
			}
		}

		list=setElement(element.elements(),list,0);
		return list;
	}

	public static Document inputUrl(String url){
		System.out.println(url);
		try {
			Document doc =Jsoup.connect(url).header("Cookie",cookie).timeout(3000).ignoreContentType(true).get();
			return doc;
		}
		catch (IOException e) {
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			return inputUrl(url);
		}
	}

	public static String getBq(String div){
		if(checkbq(div, dougan)){
			return "动画";
		}else if(checkbq(div, bangumi)){
			return "番剧";
		}else if(checkbq(div, music)){
			return "音乐";
		}else if(checkbq(div, dance)){
			return "舞蹈";
		}else if(checkbq(div, game)){
			return "游戏";
		}else if(checkbq(div, technology)){
			return "科技";
		}else if(checkbq(div, ent)){
			return "娱乐";
		}else if(checkbq(div, kichiku)){
			return "鬼畜";
		}else if(checkbq(div, movie)){
			return "电影";
		}else if(checkbq(div, teleplay)){
			return "电视剧";
		}else if(checkbq(div, fashion)){
			return "时尚";
		}else{
			return null;
		}
	}
	static String dougan[]={"MAD·AMV", "MMD·3D", "短片·手书·配音", "综合"};
	static String bangumi[]={"连载动画", "完结动画", "资讯", "官方延伸", "国产动画", "新番Index"};
	static String music[]={"翻唱", "VOCALOID·UTAU", "演奏", "三次元音乐", "同人音乐", "OP/ED/OST", "音乐选集"};
	static String dance[]={"宅舞", "三次元舞蹈", "舞蹈教程"};
	static String game[]={"单机联机", "网游·电竞", "音游", "Mugen", "GMV"};
	static String technology[]={"纪录片", "趣味科普人文", "野生技术协会", "演讲•公开课", "军事", "数码", "机械"};
	static String ent[]={"搞笑", "生活", "动物圈", "美食圈", "综艺", "娱乐圈", "Korea相关"};
	static String kichiku[]={"二次元鬼畜", "三次元鬼畜", "人力VOCALOID", "教程演示"};
	static String movie[]={"欧美电影", "日本电影", "国产电影", "其他国家", "短片", "电影相关"};
	static String teleplay[]={"连载剧集", "完结剧集", "特摄·布袋", "电视剧相关"};
	static String fashion[]={"美妆健身", "服饰", "资讯"};
	private static boolean checkbq(String em,String[] list){
		for(String ems:list){
			if(em.equals(ems)){
				return true;
			}
		}
		return false;
	}

	public static String jsoupGet(String url){
		Connection.Response connection=null;
		try {
			connection = Jsoup.connect(url).header("Cookie", cookie).timeout(3000).ignoreContentType(true).execute();
			return connection.body();
		}
		catch (IOException e) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return jsoupGet(url);
		}

	}

	public static String getProperties(String key){
		InputStream in=null;
		try {
			in = new BufferedInputStream(new FileInputStream("src\\config.properties"));
			Properties p = new Properties();
			p.load(in);
			return p.getProperty(key);
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据文件名，包名，以及字段名字段类型组成的map对象创建一个类文件
	 * @param name 文件名
	 * @param $package 包名
     * @param map 字段以及字段类型组成的键值对
     */
	public static void createClass(String name,String $package,Map<String,String> map){
		String path="src\\"+$package.replace(".","\\\\")+"\\";
		path+=name+".java";
		File file=new File(path);

		BufferedWriter bufferedWriter=null;
		try {
			file.createNewFile();
			bufferedWriter=new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
			bufferedWriter.write("package "+$package+";"+"\r\n\r\n");
			bufferedWriter.write("import java.io.Serializable;\r\n\r\n");
			bufferedWriter.write("import java.util.Date;\r\n\r\n");
			bufferedWriter.write("public class "+name+" implements Serializable{\r\n\r\n");
			for(Object key:map.keySet()){
				bufferedWriter.write("\tprivate "+map.get(key)+" "+key+";\r\n\r\n");
			}
			bufferedWriter.write("}");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			if (bufferedWriter != null) {
				try {
					bufferedWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void createTable(String tablename,String primarykey,Map<String,String> map){
		System.out.println("创建表语句\nCREATE TABLE `"+tablename+"` (");
		for(String key:map.keySet()){
			if(key.equals(primarykey)){
				System.out.println("`"+primarykey+"`  int NOT NULL ,");
			}else{
				System.out.println("`"+key+"`  "+map.get(key)+" NULL ,");
			}
		}
		System.out.println("PRIMARY KEY (`"+primarykey+"`)\n)\n;");
	}
    /**
     * 根据json对象返回字段key以及字段类型value
     * @param jsonObject json对象
     * @return 返回HashMap键值对
     */
    public static  Map<String,Map<String,String>> getFieldType(JSONObject jsonObject){
        Map<String,Map<String,String>> map=new HashMap<String, Map<String, String>>();
        //存放字段类型
        Map<String,String> mapType=new HashMap<String, String>();
        //存放数据库数据类型
        Map<String,String> mapSql=new HashMap<String, String>();
         for(Object key:jsonObject.keySet()){
                String typename=null;
                if(JSONUtils.isBoolean(jsonObject.get(key))){
                    typename=Boolean.class.getName();
                }else if(JSONUtils.isNumber(jsonObject.get(key))&&jsonObject.get(key).toString().contains(".")){
                    typename =Float.class.getName();
                }else if(JSONUtils.isNumber(jsonObject.get(key))){
                    typename =Integer.class.getName();
                }else if(checkDate(jsonObject.get(key).toString())){
                    typename =Data.class.getName();
                }else if(JSONUtils.isArray(jsonObject.get(key))){
                    typename= List.class.getName();
                }else if(JSONUtils.isObject(jsonObject.get(key))){
                    typename= Object.class.getName();
                }else{
                    typename= String.class.getName();
                }
                mapType.put(key.toString(),typename.substring(typename.lastIndexOf(".")+1));
                mapSql.put(key.toString(),Type.getValue(typename).value);
            }

        map.put("Type",mapType);
        map.put("Sql",mapSql);
        return map;
    }

    public static boolean checkDate(String date){
        try {
            new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(date);
        } catch (ParseException e) {
            try {
                new SimpleDateFormat("yyyy-MM-dd").parse(date);
            } catch (ParseException e1) {
                return false;
            }
        }
        return true;
    }


	/**
	 * 创建全字段添加语句以及更新语句
	 * @param tableName 表名
	 * @param primarykey 主键
	 * @param jsonObject 字段
     */
	public static void createSql(String tableName,String primarykey,JSONObject jsonObject){
		StringBuffer stringBuffer=new StringBuffer("INSERT INTO "+tableName+" VALUES(");
		StringBuffer stringBuffer1=new StringBuffer("UPDATE "+tableName+" SET ");
		StringBuffer stringBuffer2=new StringBuffer("(");
        for(Object key:jsonObject.keySet()){
			stringBuffer.append("#{"+key+"},");
			stringBuffer2.append(key+",");
            //除了主键之外其他字段更新
			if(!key.equals(primarykey)) {
				stringBuffer1.append(key + "=#{" + key + "},");
			}
		}

		stringBuffer2.replace(stringBuffer2.length()-1,stringBuffer2.length(),")");
		stringBuffer.replace(stringBuffer.length()-1,stringBuffer.length(),")");
		stringBuffer1.replace(stringBuffer1.length()-1,stringBuffer1.length()," WHERE "+primarykey+"=#{"+primarykey+"}");
        stringBuffer.insert(stringBuffer.indexOf("VALUES"),stringBuffer2);
		System.out.println("添加语句：\n"+stringBuffer+"\n更新语句：\n"+stringBuffer1);
	}

	public static String matchStr(String str,String regex){
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		if(matcher.find())
			return matcher.group();
		return "";

	}

	public static String matchStr(String str,String []regexs,String regex){
		for(int i=0;i<regexs.length;i++) {
			Pattern pattern = Pattern.compile(regexs[i]);
			Matcher matcher = pattern.matcher(str);
			if (matcher.find())
				return matcher.group().replaceAll(regex,"");
		}
			return "";
	}


	/*public static void getLimit(Integer i){
		i=getBiliDao().getLimit(i);
		System.out.println(i);
		if(i==null){
			return;
		}
		getBiliDao().setLimit(i);
		getLimit(i);
	}*/
}
