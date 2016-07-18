package org.pqh.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.pqh.dao.BiliDao;
import org.pqh.entity.*;
import org.pqh.test.Test;

import java.io.*;
import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BiliUtil {
	//接口 cookie信息
	public static String cookie= "";
	//模拟浏览器userAgent
	static String userAgent= getPropertie("User-Agent");
	//连接超时
	static int timeout= Integer.parseInt(getPropertie("timeout"));
	//发送的表单数据
	public static Map<String,String> formMap=new HashMap<String, String>();
	//代理服务器
	public static List<Proxy> proxyList=new ArrayList<Proxy>();

	private static Logger log=TestSlf4j.getLogger(BiliUtil.class);
	//接口一地址初始化url
	private static String aidurl="";

	/**
	 * 获取html标签里面的文本内容
	 * @param list 标签名数组
	 * @param data html数据
	 * @return 返回标签名标签值的map对象
	 */
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

	/**
	 * 把xml节点值通过反射注入到不同对象
	 * @param elements  xml文档节点列表
	 * @param list 反射的类对象
	 * @param index 反射类索引
	 * @return
	 */
	public static List setElement(List<Element> elements,List list,int index){
		Test test=new Test();
		for(org.dom4j.Element element:elements){
			if(element.elements().size()>0){
				setElement(element.elements(),list,1);
			}else{
				if(element.getName().equals("sp_title")){
					String a="";
				}
				test.setObject(list.get(index),element.getName(),element.getText());
			}

		}
		return list;
	}

	/**
	 * 爬取接口api.bilibili.com/view的信息
	 * @param aid  视频av号
	 * @param page 视频分P
	 * @return 返回接口数据注入的对象集合
	 */
	public static List setView(int aid, int page){
		if(aidurl==""){
			aidurl = Constant.AIDAPI;
		}

		SAXReader saxReader=new SAXReader();
		org.dom4j.Document document= null;
		do {
			String url=aidurl+"&id="+aid+"&page="+page;
			log.info("读取xml文档："+url);
			try {
				document = saxReader.read(url);
			} catch (DocumentException e) {
				log.info("读取xml文档："+url+"出现异常尝试重新读取");
			}
		}while (document==null);


		List list=new ArrayList();
		list.add(new Bili());
		list.add(new Bangumi());
		Element element=document.getRootElement();
		Element code=element.element("code");
		if(code!=null) {
			if (code.getText().equals("-403") || code.getText().equals("-404")||code.getText().equals("10")) {
				return null;
			} else if (code.getText().equals("-503")) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					TestSlf4j.outputLog(e,log);
				}
				return setView(aid, page);
			}
		}

		list=setElement(element.elements(),list,0);
		return list;
	}

	/**
	 * 根据视频二级分类返回一级分类
	 * @param type 二级分类
	 * @return
	 */
	public static String getBq(String type){
		if(checkbq(type, Constant.dougan)){
			return "动画";
		}else if(checkbq(type, Constant.bangumi)){
			return "番剧";
		}else if(checkbq(type, Constant.music)){
			return "音乐";
		}else if(checkbq(type, Constant.dance)){
			return "舞蹈";
		}else if(checkbq(type, Constant.game)){
			return "游戏";
		}else if(checkbq(type, Constant.technology)){
			return "科技";
		}else if(checkbq(type, Constant.ent)){
			return "娱乐";
		}else if(checkbq(type, Constant.kichiku)){
			return "鬼畜";
		}else if(checkbq(type, Constant.movie)){
			return "电影";
		}else if(checkbq(type, Constant.teleplay)){
			return "电视剧";
		}else if(checkbq(type, Constant.fashion)){
			return "时尚";
		}else{
			return null;
		}
	}

	/**
	 *
	 * @param em 二级分类
	 * @param list 二级分类列表
	 * @return 二级分类若存在返回true,否则返回flase
	 */
	private static boolean checkbq(String em,String[] list){
		for(String ems:list){
			if(em.equals(ems)){
				return true;
			}
		}
		return false;
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
		System.out.println("连接URL:"+url);
		int i=0;
		try {
			if(tClass== org.dom4j.Document.class){
				return (T) new SAXReader().read(url);
			}
			i=(int) (Math.random() * proxyList.size() - 1);

			connection = Jsoup.connect(url).header("Cookie", cookie).userAgent(userAgent).data(formMap).timeout(timeout).ignoreContentType(true);
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
			if(e.toString().contains("timed out")&&proxyList.size()>0){
				proxyList.remove(i);
				return jsoupGet(url,tClass,method);
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e1) {
				TestSlf4j.outputLog(e1,log);
			}
			return jsoupGet(url,tClass,method);
		} catch (DocumentException e) {
			throw new RuntimeException("不能识别为xml文档");
		}

	}

	/**
	 * 获取配置文件所有配置项
	 * @return
	 */
	public static Properties getProperties(){
		InputStream in=null;
		try {
			in = new BufferedInputStream(new FileInputStream("src\\config.properties"));
			Properties p = new Properties();
			p.load(in);
			return p;
		} catch (IOException e) {
			TestSlf4j.outputLog(e,log);
			return  null;
		}finally {
			try {
				in.close();
			} catch (IOException e) {
				TestSlf4j.outputLog(e,log);
			}
		}
	}


	/**
	 * 获取配置文件指定配置项
	 */
	public static String getPropertie(String key){
		return (String) BiliUtil.getProperties().get(key);
	}

	/**
	 * 从数据库同步配置
	 * @param biliDao
	 * @param key 同步的配置项
	 */
	public static void updateConfig(BiliDao biliDao,String key){
		String oldValue=BiliUtil.getPropertie(key);
		Param param=biliDao.selectParam(key);
		String newValue=param.getValue();

		File file=new File("src/config.properties");
		List<String> strings=null;
		try {
			strings= FileUtils.readLines(file);
			if(oldValue==null){
				strings.add(param.getDesc());
				strings.add(param.getKey()+"="+param.getValue());
			}else {
				for (String s : strings) {
					if (s.contains(oldValue)) {
						int index = strings.indexOf(s);
						s = s.replaceAll(oldValue, newValue);
						strings.remove(index);
						strings.add(index, s);
						break;
					}
				}
			}
			FileUtils.writeLines(file,"UTF-8",strings);
		} catch (IOException e) {
			TestSlf4j.outputLog(e,log);
		}
	}

	/**
	 * 从数据库创建配置项
	 * @param biliDao
	 * @param file 生成的配置文件对象
	 */
	public static void createConfig(BiliDao biliDao,File file){
		List<Param> list=biliDao.selectParams();
		List<String> stringList=new ArrayList<String>();
		for(Param param:list){
			stringList.add(param.getDesc());
			stringList.add(param.getKey()+"="+(param.getValue()!=null?param.getValue():""));
		}
		try {
			FileUtils.writeLines(file,"GBK",stringList);
		} catch (IOException e) {
			TestSlf4j.outputLog(e,log);
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
			TestSlf4j.outputLog(e,log);
		} catch (IOException e) {
			TestSlf4j.outputLog(e,log);
		}finally {
			if (bufferedWriter != null) {
				try {
					bufferedWriter.close();
				} catch (IOException e) {
					TestSlf4j.outputLog(e,log);
				}
			}
		}
	}

	/**
	 * 生成创建表sql
	 * @param tablename 表名
	 * @param primarykey 表主键
	 * @param map 存放表字段，字段类型map对象
	 */
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

	/**
	 * 检查日期格式有效性
	 * @param date 日期
	 * @return
	 */
	private static boolean checkDate(String date){
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

	/**
	 * 正则表达式匹配
	 * @param str 匹配字符串
	 * @param regex 正则表达式
	 * @param c 返回类型
	 * @param <T>
	 * @return
	 */
	public static <T>T matchStr(String str,String regex,Class<T> c){
		List list=new ArrayList();
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		while (matcher.find()) {
			if(c==String.class) {
				return (T) matcher.group();
			}else if(c==List.class){
				list.add(matcher.group());
			}else{
				throw  new RuntimeException(c.getClass().getName()+"参数类型错误");
			}
		}
		return (T) list;

	}

	/**
	 * 正则表达式匹配内容以外的结果
	 * @param str 匹配字符串
	 * @param regexs 正则表达式数组
	 * @param regex
	 * @return
	 */
	public static String matchStr(String str,String []regexs,String regex){
		for(int i=0;i<regexs.length;i++) {
			Pattern pattern = Pattern.compile(regexs[i]);
			Matcher matcher = pattern.matcher(str);
			while (matcher.find()) {
				return matcher.group().replaceAll(regex, "");
			}
		}
		return "";
	}

	/**
	 * 调用window图片查看器打开图片
	 * @param file 图片文件对象
	 */
	public static void openImage(File file){
		try {
			Runtime.getRuntime().exec("rundll32 c:\\Windows\\System32\\shimgvw.dll,ImageView_Fullscreen "+file.getAbsoluteFile());
		} catch (IOException e) {
			TestSlf4j.outputLog(e,log);
		}
	}

	/**
	 * 获取代理服务器
	 * @param address 服务器地址
	 */
	public static void addProxy(String address){
		Document document = BiliUtil.jsoupGet(Constant.PROXYURL, Document.class, Constant.GET);
		Elements elements=document.select("#ip_list tr:gt(0)");
		for(org.jsoup.nodes.Element element:elements){
//			System.out.println(element.select("td:eq(1)")+"\t"+element.select("td:eq(2)"));
			SocketAddress socketAddress = new InetSocketAddress(element.select("td:eq(1)").text(), Integer.parseInt(element.select("td:eq(2)").text()));
			if(element.select("td:eq(3)").text().equals(address)) {
				Proxy proxy = new Proxy(Proxy.Type.HTTP, socketAddress);
				BiliUtil.proxyList.add(proxy);
			}
		}
	}

	public static List<Data> parseTable(File file){
		List<Data> dataList = new ArrayList<Data>();
		try {
			Document document = Jsoup.parse(file,"UTF-8");
			Elements elements = document.select("th");
			Map<Integer, String> map = new HashMap<Integer, String>();
			for (org.jsoup.nodes.Element element : elements) {
				map.put(elements.indexOf(element), element.text());
			}
			elements = document.select("tr:gt(0)");
			for (org.jsoup.nodes.Element element : elements) {
				Data data = new Data();
				for (org.jsoup.nodes.Element td : element.select("td")) {
					String filedName = map.get(element.select("td").indexOf(td));
					Field field = data.getClass().getDeclaredField(filedName);
					field.setAccessible(true);
					if (field.getType() == Integer.class) {
						field.set(data, Integer.parseInt(td.text()));
					} else {
						field.set(data, td.text());
					}

				}
				dataList.add(data);
			}
		} catch (IllegalAccessException e) {
			TestSlf4j.outputLog(e,log);
		} catch (IOException e) {
			TestSlf4j.outputLog(e,log);
		} catch (NoSuchFieldException e) {
			TestSlf4j.outputLog(e,log);
		}
		return dataList;
	}

	/**
	 * httpclient get请求封装
	 * @param href
	 * @return
     */
	public static CloseableHttpResponse doGet(String href){
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(href);
		CloseableHttpResponse closeableHttpResponse= null;
		try {
			closeableHttpResponse = closeableHttpClient.execute(httpGet);
		} catch (IOException e) {
			TestSlf4j.outputLog(e,log);
		}
		return closeableHttpResponse;
	}



	/**
	 * 获取Accesskey
	 * @param username bilibili帐号
	 * @param pwd bilibili密码
	 * @return
	 */
	public static String getAccesskey(String username,String pwd) {
		if ("".equals(getPropertie("biliusername")) || "".equals(getPropertie("bilipwd"))) {
			throw new RuntimeException("bilibili账号和密码不能为空");
		}

		String appkey=getPropertie("appkey");
		String	app_secret=getPropertie("app_secret");
		try {
			username= URLEncoder.encode(username,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			TestSlf4j.outputLog(e,log);
		}

		String cs="appkey=" + appkey +"&captcha=&platform=ios&pwd="+pwd +"&type=json&userid="+username ;
		String sign=MD5Util.MD5(cs+app_secret).toLowerCase();

		String url=Constant.ACCESS_KEY+cs+"&sign="+ sign;
		JSONObject jsonObject=BiliUtil.jsoupGet(url,JSONObject.class,Constant.GET);
		String code=jsonObject.get("code").toString();
		if(code.equals("-626")){
			throw new RuntimeException("账号:"+username+"不存在");
		}
		else if(code.equals("-627")){
			throw new RuntimeException("密码:"+pwd+"错误");
		}

		else if(jsonObject.get("code").toString().equals("0")){
			return jsonObject.get("access_key").toString();
		}else{
			throw new RuntimeException("未知错误");
		}
	}

}
