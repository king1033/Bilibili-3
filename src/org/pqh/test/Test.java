package org.pqh.test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.runner.RunWith;
import org.pqh.dao.BiliDao;
import org.pqh.dao.VstorageDao;
import org.pqh.entity.Data;
import org.pqh.entity.Param;
import org.pqh.entity.Vstorage;
import org.pqh.service.AvCountService;
import org.pqh.service.InsertService;
import org.pqh.service.InsertServiceImpl;
import org.pqh.util.*;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

/**
 * Created by Reborn on 2016/2/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class Test {
    private static Logger log= TestSlf4j.getLogger(Test.class);
    @Resource
    ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    BiliDao biliDao;
    @Resource
    VstorageDao vstorageDao;
    @Resource
    InsertService insertService;
    @Resource
    AvCountService avCountService;

    public static void main(String[] args) throws Exception {
//        FindResourcesUtil.dLQrcode("http://ww3.sinaimg.cn/large/006xdUelgw1f6450qiwjkg30o40dkb2r.gif","C:\\Users\\10295\\OneDrive\\图片\\屏幕快照\\2.jpg","200");
//        BiliUtil.formMap=BiliUtil.parseXml(Constant.BduSendMsg);
//        BiliUtil.formMap.put("msg",TimeUtil.formatDateToString(new Date(),Constant.DATETIME)+"测试");
//        BiliUtil.cookie=PropertiesUtil.getProperties("baiducookie");
//        Document document=BiliUtil.jsoupGet(Constant.BduSendMsg,Document.class,Constant.POST);
    }

    /**
     * 测试各种方法
     */
    @org.junit.Test
    public void testMethod() {
//        Map<String,List<BtAcg>> map=FindResourcesUtil.findBy_Btacg(threadPoolTaskExecutor,"银魂");
//        Map<String,String> hrefMap=FindResourcesUtil.screenUrl(map,new BtAcg("BDRIP",null,null));
//        Map<String,String> map=new HashMap<String, String>();
//        map.put("title","网球王子");
//        map.put("typeid","32,33");
//        downLoadDanMu(map,"弹幕",2);


    }
    @org.junit.Test
    public void testTask(){
        ThreadUtil.threadRun(Test.class,new String[]{"testView","runCrawler"});
        while(true){
            ThreadUtil.sleep(log,3600*1000*24);
        }
    }

    /**
     * 检查ID有效性
     * @param map ID存放的map
     */
    public void checkId(Map<String,String> map){
        int count=0;
        for(String key:map.keySet()) {
            if(key.contains("id")) {
                String error=map.get(key)+"：不合法ID参数,ID参数正确格式应该是纯数字，如果是多个ID则数字之间要用逗号隔开";
                if(map.get(key).indexOf(",")==-1&&map.get(key).replaceAll("\\D","").length()==0){
                    throw new RuntimeException(error);
                }
                for (String s : map.get(key).split(",")) {
                    if (s.replaceAll("\\D", "").length() == 0) {
                        throw new RuntimeException(error);
                    } else {
                        count++;
                    }
                }
                log.info(key+"参数共检测出" + count + "个ID准备拼接到sql语句里面进行查询");
            }

        }

    }

    /**
     * 自动添加或更新配置项
     * @param biliDao
     * @param key
     * @param value
     * @param desc
     */
    public  void createParam(BiliDao biliDao,String key,String value,String desc){
        desc=gbEncoding(desc);
        Param param=biliDao.selectParam(key);
        if(param!=null){
            param.setValue(value);
            param.setDesc("#"+desc);
            biliDao.updateParam(param);
        }else{
            param=new Param(key,value,"#"+desc);
            biliDao.insertParam(param);
        }
        PropertiesUtil.createConfig(biliDao,new File("src/config.properties"));
    }
    /**
     * 字符换uncoide编码
     * @param gbString
     * @return
     */
    public static String gbEncoding(final String gbString) {
        char[] utfBytes = gbString.toCharArray();
        String unicodeBytes = "";
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
            String hexB = Integer.toHexString(utfBytes[byteIndex]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        log.info(gbString+"\tunicodeBytes is: " + unicodeBytes);
        return unicodeBytes;
    }
    /**
     * 查询条件
     * @param map
     */
    public void downLoadDanMu(Map<String,String> map,String dirPath,int type){
        long a=System.currentTimeMillis();
        this.checkId(map);
        log.info("查询参数"+map);
        List<Data> dataList=null;
        switch (type){
            case 1:dataList=vstorageDao.selectData(map);break;
            case 2:dataList=vstorageDao.selectDataCid(map);break;
            default:throw new RuntimeException("不存在第"+type+"条查询语句");
        }
        long b=System.currentTimeMillis();
        log.info("查询耗费时间"+TimeUtil.longTimeFormatString(b-a));
        Map<String,List<Data>> listMap=new HashMap<String, List<Data>>();
        for(Data data:dataList){
            String dirname= FindResourcesUtil.switchFileName(data.getTitle());
            if(listMap.get(dirname)==null){
                listMap.put(dirname,new ArrayList<Data>());
            }
            listMap.get(dirname).add(data);
        }
        for(String dirName:listMap.keySet()){
            dataList=listMap.get(dirName);
            for(Data data:dataList){
                String path=dirName;
                if(dataList.size()>1){
                    if(data.getSubtitle()!=null){
                        path+="/"+FindResourcesUtil.switchFileName(data.getSubtitle());
                    }else{
                        path+="/"+data.getCid()+"";
                    }
                }
                DownLoadUtil.downLoad(Constant.DANMU+data.getCid()+".xml",dirPath+"/"+path+".xml");
            }
            File file=new File(dirPath+"/"+dirName);
            if(file.isDirectory()) {
                int fileCount = file.listFiles().length;
                if (fileCount == 0) {
                    try {
                        FileUtils.deleteDirectory(file);
                    } catch (IOException e) {
                        TestSlf4j.outputLog(e,log);
                    }
                }
            }
        }
    }
    /**
     * 获取动画开播日期
     * @param document
     * @return
     */
    public static String getInfo(Document document){
        if(document.select("ul.polysemantList-wrapper .selected").text().contains("动画")){
            Elements elements=document.select("div.basic-info>dl>dt");
            for(Element element:elements){
                if(element.text().equals("播放期间")){
                    int index=elements.indexOf(element);
                    element=document.select("div.basic-info>dl>dd").get(index);
                    return element.text();
                }
            }
        }
        Elements elements=document.select("ul.polysemantList-wrapper>.item>a");
        for(Element element:elements){
            if(element.attr("title").length()!=0&&element.attr("title").contains("动画")){
                log.info(element.attr("title")+"跳转到动画条目"+Constant.BAIKEINDEX+element.attr("href"));
                return getInfo(CrawlerUtil.jsoupGet(Constant.BAIKEINDEX+element.attr("href"),Document.class, Constant.GET));
            }
        }
        return "";
    }

    /**
     * 压缩备份文件
     * @param _7zFile 压缩包文件
     * @param sqlFile 数据库文件
     */
    public static void compress(File _7zFile,File sqlFile){
        String command="7z a -t7z "+_7zFile.getAbsolutePath()+" "+sqlFile.getAbsolutePath()+" -mx=9 -m0=LZMA2:a=2:d=26 -ms=4096m -mmt -p"+PropertiesUtil.getProperties("7zpwd",String.class);
        runCommand(command,false);
    }

    /**
     * 删除旧的备份文件
     * @param date 比较的时间
     * @param dir 备份文件目录
     */
    public  static void delOldFile(Date date,String dir){
        Collection<File> fileList=FileUtils.listFiles(new File(dir),new String[]{"sql"},true);
        for(File file:fileList){
            if(FileUtils.isFileOlder(file,date)){
                log.info("删除旧备份文件"+file.getAbsoluteFile());
                file.delete();
            }
        }
    }

    /**
     * 删除junit产生的临时文件
     */
    public void deleteTestTemp(){
        //临时文件，选中扩展名为out格式的文件
        Collection<File> fileList=FileUtils.listFiles(FileUtils.getTempDirectory(),new String[]{"out"},true);
        for(File file:fileList){
            //确认是idea产生的临时文件则删除
            if(file.getName().contains("idea")){
                file.delete();
            }
        }
    }

    public static void uploadBdu(String path){
        String command="G:\\Projects\\bypy\\bypy upload "+path;
        runCommand(command,false);
    }

    /**
     * 备份数据库
     */
    public  void saveDataBase(){
        BiliUtil.openImage(new File("WebContent/image/dbbackup.jpg"));
        ThreadPoolTaskExecutor threadPoolTaskExecutor=SpringContextHolder.getBean("taskExecutor");
        Date date=new Date();
        String date_1=TimeUtil.formatDateToString(date,"HH_mm_ss");
        String date_2=TimeUtil.formatDateToString(date,null);
        Calendar c=Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE,-1);
        Date date1=c.getTime();
        String date_3=TimeUtil.formatDateToString(date1,null);
        //当前日期年月日作为备份数据库的目录
        String localPath=PropertiesUtil.getProperties("localPath",String.class);
        String todayDir=localPath+date_2+"\\";
        //昨天备份文件目录
        String yesterday=localPath+date_3+"\\";
        //当前日期时分秒作为备份数据库文件的文件名
        File sqlFile=new File(todayDir+date_1+".sql");
        File oldDir=new File(yesterday);
        //调用mysqldump备份命令备份数据库
        File batFile=new File(todayDir+date_1+".bat");
        //运行备份命令
        String command="\""+PropertiesUtil.getProperties("mysqlPath",String.class)+"bin/mysqldump\" --default-character-set=utf8 -u"+PropertiesUtil.getProperties("dbusername",String.class)+" -p"+PropertiesUtil.getProperties("dbpassword",String.class)+" bilibili "+PropertiesUtil.getProperties("backuptables",String.class)+">"+sqlFile.getAbsolutePath();
        try {
            FileUtils.writeStringToFile(new File("test.bat"),command,"GBK");
        } catch (IOException e) {
            e.printStackTrace();
        }
        new File(todayDir).mkdir();
        runCommand("test.bat",true);
        TaskShowImg taskShowImg=new TaskShowImg("数据库于"+TimeUtil.formatDateToString(new Date(),Constant.DATETIME)+"备份到"+sqlFile.getAbsolutePath());
        threadPoolTaskExecutor.execute(taskShowImg);
        //每天定时打包一次数据库放到服务器
        File _7zFile=new File(PropertiesUtil.getProperties("serverPath",String.class)+date_2+"\\"+date_1+".7z");
        //打包sql文件
        compress(_7zFile,sqlFile);
        taskShowImg=new TaskShowImg("数据库于"+TimeUtil.formatDateToString(new Date(),Constant.DATETIME)+"打包到"+_7zFile.getAbsolutePath());
        threadPoolTaskExecutor.execute(taskShowImg);
        //上传sql到百度云
        uploadBdu(_7zFile.getAbsolutePath());
        taskShowImg=new TaskShowImg("数据库于"+TimeUtil.formatDateToString(new Date(),Constant.DATETIME)+"上传到百度云");
        threadPoolTaskExecutor.execute(taskShowImg);
        File old7zFile=new File(PropertiesUtil.getProperties("serverPath",String.class)+date_3+"\\");
        //删除旧备份打包文件
        FileUtils.deleteQuietly(old7zFile);
        //删除旧备份目录
        FileUtils.deleteQuietly(oldDir);
    }

    /**
     * 调用命令行运行命令
     * @param command 运行命令
     * @param flag 是否保留命令行文件
     */
    public static void  runCommand(String command,boolean flag){
        Process ps = null;
        InputStreamReader ir=null;
        BufferedReader br=null;
        InputStream in=null;
        try {
            long a=System.currentTimeMillis();
            ps=Runtime.getRuntime().exec(command);
            in=ps.getInputStream();
            ir = new InputStreamReader(in,"GBK");
            br = new BufferedReader(ir);
            String line;
            while ((line = br.readLine()) != null) {
                if(line.length()>0) {
                    log.info(line);
                }
            }
            long b=System.currentTimeMillis();
            log.info("运行命令花费时间"+TimeUtil.longTimeFormatString(b-a));

        } catch (IOException e) {
            TestSlf4j.outputLog(e,log);
        }finally {
            try {
                in.close();
                ir.close();
                br.close();
                System.gc();
                File file=new File(command);
                if(file.exists()&&flag){
                    file.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 把json对象存进map里面
     * @param jsonObject json对象
     * @param map json对象转换的实体类字典
     * @param classname 根节点名称
     * @param flag 反射操作对象是否为集合的标记
     * @param index 反射当前集合某个索引的对象
     * @param cid
     * @return
     */
    public Map getMap(JSONObject jsonObject,Map map,String classname,boolean flag,int index,int cid){
        for(Object key:jsonObject.keySet()) {
            if (JSONUtils.isArray(jsonObject.get(key))) {
                JSONArray jsonArray = jsonObject.getJSONArray(key.toString());
                boolean flag_1=NodeUtil.getChildNode(classname,key.toString()).equals(String.class.getName());
                classname = flag_1?classname:NodeUtil.getChildNode(classname,key.toString());
                if(flag_1){
                    String value=parseByJackson(Constant.VSTORAGEAPI+cid,key.toString());
                    map.put(classname, ReflexUtil.setObject(map.get(Data.class.getName()), key.toString(),value));
                    continue;
                }
                for (int i=0;i<jsonArray.size();i++) {
                    index = i;
                    ((List)map.get(classname)).add(ReflexUtil.getObject(classname));
                    getMap(JSONObject.fromObject(jsonArray.get(i)), map, classname,true,index,cid);
                    ReflexUtil.setObject(((List)map.get(classname)).get(index),"cid",String.valueOf(cid));
                    ReflexUtil.setObject(((List)map.get(classname)).get(index),"id",String.valueOf(i+1));
                }
                classname=NodeUtil.getParentsNode(classname);
            } else if (JSONUtils.isObject(jsonObject.get(key))&&!jsonObject.get(key).equals(null)) {
                JSONObject object = jsonObject.getJSONObject(key.toString());
                JSONObject jsonObject1 = jsonObject;
                String name = classname;
                classname = NodeUtil.getChildNode(classname,key.toString());
                getMap(object, map, classname,false,index,cid);
                ReflexUtil.setObject(map.get(classname),"cid",String.valueOf(cid));
                jsonObject = jsonObject1;
                classname = NodeUtil.getParentsNode(classname);
            } else {
                String value = jsonObject.get(key).toString();
                if(flag){
                    Object o=((List)map.get(classname)).get(index);
                    ReflexUtil.setObject(o, key.toString(), value);
                }else {
                    map.put(classname, ReflexUtil.setObject(map.get(classname), key.toString(), value));
                }
            }
        }
        if(classname.equals(Vstorage.class.getName())) {
            map.put(classname, ReflexUtil.setObject(map.get(classname), "id", String.valueOf(cid)));
        }
        return map;
    }

    /**
     * 通过反射把相应的爬虫数据写入数据库不同表
     * @param vstorageDao
     * @param map
     */
    public void setData(VstorageDao vstorageDao,Map<String,Object> map){
        Class c=vstorageDao.getClass();
        String name=null;
        String classnames[] = PropertiesUtil.getProperties("exclude",String.class).split(",");
        for(String classname:classnames){
            map.remove(classname);
        }

        for(String key:map.keySet()) {
            Method insertMethod = null;
            Method updateMethod = null;
            try {
                name = key.substring(key.lastIndexOf(".") + 1);
                insertMethod = c.getDeclaredMethod("insert" + name, Class.forName(key));
                updateMethod = c.getDeclaredMethod("update" + name, Class.forName(key));
            } catch (NoSuchMethodException e) {
                TestSlf4j.outputLog(e,log);
            } catch (ClassNotFoundException e) {
                TestSlf4j.outputLog(e,log);
            }
            if (map.get(key).getClass().getName().contains("List")) {
                List list = (List) map.get(key);
                if (list.size() == 0) {
                    continue;
                }
                for (Object object : (List) map.get(key)) {
                    if (ReflexUtil.checkFieldsNaN(object)) {
                        continue;
                    }
                    try {
                        insertMethod.invoke(vstorageDao, object);
                    } catch (InvocationTargetException e) {
                        Field field=null;
                        String detailMessage=null;
                        try {
                            field=Throwable.class.getDeclaredField("detailMessage");
                            field.setAccessible(true);
                            detailMessage=field.get(e.getTargetException().getCause()).toString();
                            detailMessage=BiliUtil.matchStr(detailMessage,"\\d+\\-\\d+",String.class);
                            if(detailMessage.length()!=0){
                                log.info("更新"+name+"复合主键："+detailMessage+"信息");
                                updateMethod.invoke(vstorageDao, object);
                            }
                        } catch (NoSuchFieldException e1) {
                            log.error(object+"无法获取详细报错信息！！！");
                        } catch (IllegalAccessException e1) {
                            TestSlf4j.outputLog(e1,log);
                        } catch (InvocationTargetException e1) {
                            TestSlf4j.outputLog(e1,log);
                        }
                    } catch (IllegalAccessException e) {
                        TestSlf4j.outputLog(e,log);
                    }
                }

            } else {
                if (ReflexUtil.checkFieldsNaN(map.get(key))) {
                    continue;
                }
                try {
                    insertMethod.invoke(vstorageDao, map.get(key));
                } catch (InvocationTargetException e) {
                    if(e.getTargetException().getClass().equals(DuplicateKeyException.class)){
                        log.info("更新"+name+"主键："+key+"信息");
                    }else{
                        TestSlf4j.outputLog(e,log);
                    }
                } catch (IllegalAccessException e) {
                    TestSlf4j.outputLog(e,log);
                }
            }
        }
    }



    /**
     * JSONObject把某个key的内容解析错误之后用Jackson去解析
     * @param url
     * @param key json属性
     * @return 返回正确属性值
     */
    public static String parseByJackson(String url,String key) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map data = objectMapper.readValue(new URL(url), Map.class);
            Map map1 = null;
            if (data.get("data") != null) {
                map1 = ((Map) data.get("data"));
            }
            if (map1 != null) {
                return map1.get(key) == null ? "" : map1.get(key).toString();
            }

        } catch (JsonParseException e) {
            TestSlf4j.outputLog(e,log);
        } catch (JsonMappingException e) {
            TestSlf4j.outputLog(e,log);
        } catch (MalformedURLException e) {
            TestSlf4j.outputLog(e,log);
        } catch (IOException e) {
            TestSlf4j.outputLog(e,log);
        }
        return  null;
    }


    public void addTask(int id,String methodName){
        final InsertService insertService= SpringContextHolder.getBean("insertServiceImpl");
        final BiliDao biliDao=SpringContextHolder.getBean("biliDao");
        final ThreadPoolTaskExecutor threadPoolTaskExecutor=SpringContextHolder.getBean("taskExecutor");
        TaskCid.insertService=insertService;
        for (int cid = biliDao.getAid(id);;cid++) {
            if(id==2&&InsertServiceImpl.count>=10){
                cid=biliDao.getAid(id);
                InsertServiceImpl.count=0;
            }else if(id==3&&InsertServiceImpl.count_>=10){
                cid=biliDao.getAid(id);
                InsertServiceImpl.count_=0;
            }
            TaskCid taskCid=new TaskCid(cid,methodName);
            while (threadPoolTaskExecutor.getThreadPoolExecutor().getQueue().size()>100){
                ThreadUtil.sleep(log,1000);
            }
            excute(threadPoolTaskExecutor,taskCid);
        }
    }

    @org.junit.Test
    public void runCrawler() {
        Thread thread_1=new Thread(new Runnable() {
            @Override
            public void run() {
                addTask(3,"insertVstorage");
            }
        });
        Thread thread_2=new Thread(new Runnable() {
            @Override
            public void run() {
                addTask(2,"insertCid");
            }
        });
        thread_1.start();
        thread_2.start();
    }

    @org.junit.Test
    public void testView(){
        InsertService insertService= SpringContextHolder.getBean("insertServiceImpl");
        BiliDao biliDao=SpringContextHolder.getBean("biliDao");
        insertService.insertBili(biliDao.getAid(1),1);
    }


    /**
     * 多线程执行任务简单封装
     * @param threadPoolTaskExecutor
     * @param runnable
     */
    public void excute(ThreadPoolTaskExecutor threadPoolTaskExecutor,Runnable runnable){
        threadPoolTaskExecutor.execute(runnable);
        ThreadUtil.sleep(log,100);
    }


}
