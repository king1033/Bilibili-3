package org.pqh.util;

import net.sf.json.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.pqh.entity.Bangumi;
import org.pqh.entity.Bili;
import org.pqh.test.Test;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BiliUtil {
	
	private static Logger log=TestSlf4j.getLogger(BiliUtil.class);
	//接口一地址初始化url
	private static String aidurl="";

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
				ReflexUtil.setObject(list.get(index),element.getName(),element.getText());
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
			aidurl = Constant.AIDAPI+BiliUtil.getAccesskey(PropertiesUtil.getProperties("biliusername",String.class),PropertiesUtil.getProperties("bilipwd",String.class));
		}
		org.dom4j.Document document=null;
		String url=aidurl+"&id="+aid+"&page="+page;
		document = CrawlerUtil.jsoupGet(url, org.dom4j.Document.class,Constant.GET);
		if(document==null){
			return null;
		}
		List list=new ArrayList();
		list.add(new Bili());
		list.add(new Bangumi());
		Element element=document.getRootElement();
		Element code=element.element("code");
		if(code!=null) {
			if (code.getText().equals("-403") || code.getText().equals("-404")||code.getText().equals("10")) {
				return null;
			} else if (code.getText().equals("-503")) {
				ThreadUtil.sleep(log,3000);
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
	 * 使同一个文件夹里的视频跟对应的字幕的文件名一致
	 * @param dirpath 需要修改文件名的目录
	 * @param suffix  被同步的字幕文件格式
	 * @param size  视频体积过滤大小单位MB
     */
	public static void replaceFileName(String dirpath,String suffix,int size){
		File file=new File(dirpath);
		List<File> subtitle= (List<File>) FileUtils.listFiles(file, FileFilterUtils.suffixFileFilter(suffix),null);
		List<File> video= (List<File>) FileUtils.listFiles(file,FileFilterUtils.sizeFileFilter(1024*1024*size),null);
		if(subtitle.size()!=video.size()){
			throw  new RuntimeException("视频文件数与弹幕文件数不一致无法同步文件名");
		}
		for(int i=0;i<video.size();i++){
			try {
				String path=video.get(i).getAbsolutePath();
				String _suffix=path.substring(path.lastIndexOf("."));
				FileUtils.moveFile(subtitle.get(i),new File(path.replace(_suffix,"."+suffix)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
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
	 *从文件解析数据
	 * @param tClass
	 * @param filepath
	 * @param <T>
	 * @return
	 */
	public static <T>T parseFile(Class<T> tClass,String filepath){
		try {
			List<String> strings=FileUtils.readLines(new File(filepath));
			T t=tClass.newInstance();
			for(String s:strings){
				if(tClass==ArrayList.class){
					((ArrayList) t).add(s);
				}else if(tClass==HashMap.class){
					((HashMap) t).put(s.split(":")[0],s.split(":")[1]);
				}
			}
			return t;
		} catch (IOException e) {
			TestSlf4j.outputLog(e,log);
		} catch (InstantiationException e) {
			TestSlf4j.outputLog(e,log);
		} catch (IllegalAccessException e) {
			TestSlf4j.outputLog(e,log);
		}
		throw new RuntimeException("解析文件出错");
	}

	public static Map<String,String> parseXml(String url){
		Map<String,String> map=new HashMap<String, String>();
		org.dom4j.Document xml= null;
		try {
			xml = new SAXReader().read("src/formparam.xml");
		} catch (DocumentException e) {
			parseXml(url);
		}
		List<org.dom4j.Element> forms=xml.getRootElement().elements();
		for(org.dom4j.Element form:forms){
			if(form.attribute("url").getStringValue().equals(url)){
				for(org.dom4j.Element param:form.elements()){
					map.put(param.getName(),param.getStringValue());
				}
				return map;
			}
		}
		throw new RuntimeException("找不到"+url+"的表单参数");
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
		Document document = CrawlerUtil.jsoupGet(Constant.PROXYURL, Document.class, Constant.GET);
		Elements elements=document.select("#ip_list tr:gt(0)");
		for(org.jsoup.nodes.Element element:elements){
//			System.out.println(element.select("td:eq(1)")+"\t"+element.select("td:eq(2)"));
			//爬取代理ip以及端口号
			SocketAddress socketAddress = new InetSocketAddress(element.select("td:eq(1)").text(), Integer.parseInt(element.select("td:eq(2)").text()));
			//匹配指定区域的代理
			if(element.select("td:eq(3)").text().equals(address)) {
				Proxy proxy = new Proxy(Proxy.Type.HTTP, socketAddress);
				//把符合条件的代理添加到代理集合
				CrawlerUtil.proxyList.add(proxy);
			}
		}
	}




	/**
	 * 获取Accesskey
	 * @param username bilibili帐号
	 * @param pwd bilibili密码
	 * @return
	 */
	public static String getAccesskey(String username,String pwd) {
		if ("".equals(PropertiesUtil.getProperties("biliusername",String.class)) || "".equals(PropertiesUtil.getProperties("bilipwd",String.class))) {
			throw new RuntimeException("bilibili账号和密码不能为空");
		}

		String appkey= PropertiesUtil.getProperties("appkey",String.class);
		String	app_secret= PropertiesUtil.getProperties("app_secret",String.class);
		try {
			username= URLEncoder.encode(username,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			TestSlf4j.outputLog(e,log);
		}

		String cs="appkey=" + appkey +"&captcha=&platform=ios&pwd="+pwd +"&type=json&userid="+username ;
		String sign= AlgorithmUtil.MD5(cs+app_secret).toLowerCase();

		String url=Constant.ACCESS_KEY+cs+"&sign="+ sign;
		JSONObject jsonObject=CrawlerUtil.jsoupGet(url,JSONObject.class,Constant.GET);
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
