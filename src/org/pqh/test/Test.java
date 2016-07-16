package org.pqh.test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.deploy.net.HttpResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.DeflateDecompressingEntity;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.dom4j.io.SAXReader;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.runner.RunWith;
import org.pqh.dao.BiliDao;
import org.pqh.dao.VstorageDao;
import org.pqh.entity.*;
import org.pqh.service.AvCountService;
import org.pqh.service.InsertService;
import org.pqh.service.InsertServiceImpl;
import org.pqh.util.BiliUtil;
import org.pqh.util.FindResourcesUtil;
import org.pqh.util.TestSlf4j;
import org.pqh.util.TsdmUtil;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.GZIPInputStream;

/**
 * Created by Reborn on 2016/2/5.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class Test {
    private static Logger log= TestSlf4j.getLogger(Test.class);
    public static String url="http://api.bilibili.com/vstorage/state?cid=";
    @Resource
    ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Resource
    BiliDao biliDao;
    @Resource
    InsertService insertService;
    @Resource
    AvCountService avCountService;

    public static void main(String[] args) throws Exception {
        Test test=new Test();
        //getUrl("669933500564212");
//        test.saveDataBase();
//        Map<String,List<BtAcg>> map=FindResourcesUtil.findBy_Btacg("伊莉雅");
//        List list=map.get("完整动画");
//        Collections.sort(list,new ComparatorAvPlay("size"));
//        String filename=FindResourcesUtil.switchFileName(btAcg.getResourceName());
//        FindResourcesUtil.downLoadTorrent("http://www.kuaipic.com/uploads/userup/231761/ef13541d77e923aeb125.jpg", FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath());
//         FindResourcesUtil.downLoadTorrent("http://comment.bilibili.com/3974749.xml","G:\\");

    }

    @org.junit.Test
    public void testMethod() {


    }

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
                log.info(element.attr("title")+"跳转到动画条目http://baike.baidu.com"+element.attr("href"));
                return getInfo(BiliUtil.jsoupGet("http://baike.baidu.com"+element.attr("href"), Document.class,BiliUtil.GET));
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
        List<String> list=new ArrayList<String>();
        list.add("7z a -t7z "+_7zFile.getAbsolutePath()+" "+sqlFile.getAbsolutePath()+" -mx=9 -m0=LZMA2:a=2:d=26 -ms=4096m -mmt -pA班姬路");
        File file=new File(sqlFile.getParent()+"\\Test.bat");
        try {
            FileUtils.writeLines(file,"GBK",list);
        } catch (IOException e) {
            log.error("错误类型："+e.getClass()+"\t错误信息"+e.getCause().getMessage());
        }
        runCommand(file.getAbsolutePath());
        file.delete();
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
     * 日期格式化
     * @param date 日期
     * @param format 格式
     * @return 返回格式化日期
     */
    public static String format(Date date,String format){
        return new SimpleDateFormat(format).format(date);
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

    /**
     * 备份数据库
     */
    public  void saveDataBase(){
        BiliUtil.openImage(new File("WebContent/dbbackup.jpg"));
        List<String> list=new ArrayList<String>();
        Date date=new Date();
        String date_1=format(date,"HH_mm_ss");
        String date_2=format(date,"yyyy_MM_dd");
        Calendar c=Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE,-1);
        Date date1=c.getTime();
        String date_3=format(date1,"yyyy_MM_dd");
        //当前日期年月日作为备份数据库的目录
        String todayDir=BiliUtil.getPropertie("localPath")+date_2+"\\";
        String yesterday=BiliUtil.getPropertie("localPath")+date_3+"\\";
        //当前日期时分秒作为备份数据库文件的文件名
        File sqlFile=new File(todayDir+date_1+".sql");
        File oldDir=new File(yesterday);
        //调用mysqldump备份命令备份数据库
        list.add("\"K:\\MySQL\\MySQL Server 5.7\\bin\\mysqldump\" --opt -uroot -p123456 bilibili data cid> "+sqlFile.getAbsolutePath());
        File batFile=new File(todayDir+date_1+".bat");
        try {
            FileUtils.writeLines(batFile,"GBK",list);
        } catch (IOException e) {
            log.error("错误类型："+e.getClass()+"\t错误信息"+e.getCause().getMessage());
        }
        //命令写进文件进行备份
        Test.runCommand(batFile.getAbsolutePath());
        //命令结束删除
        batFile.delete();
        delOldFile(date,todayDir);
        //每天凌晨三点打包一次数据库放到服务器
        File _7zFile=new File(BiliUtil.getPropertie("serverPath")+date_2+"\\"+date_1+".7z");
        compress(_7zFile,sqlFile);
        File old7zFile=new File(BiliUtil.getPropertie("serverPath")+date_3+"\\");
        FileUtils.deleteQuietly(old7zFile);
        FileUtils.deleteQuietly(oldDir);
    }

    /**
     * 调用命令行运行命令
     * @param command 运行命令
     */
    public static void runCommand(String command){
        Process ps = null;
        try {
            long a=System.currentTimeMillis();
            ps=Runtime.getRuntime().exec(command);
            InputStreamReader i = new InputStreamReader(ps.getInputStream(),"GBK");
            String line;
            BufferedReader ir = new BufferedReader(i);
            while ((line = ir.readLine()) != null) {
                if(line.length()>0) {
                    log.info(line);
                }
            }
            long b=System.currentTimeMillis();
            log.info("运行命令花费时间"+(b-a)+"ms");
        } catch (IOException e) {
            log.error("错误类型："+e.getClass()+"\t错误信息"+e.getCause().getMessage());
        }
    }
    /**
     * 为对象的指定属性赋值
     * @param object
     * @param key
     * @param value
     * @return
     */
    public Object setObject(Object object,String key,String value) {
        Field field=null;
        try {
            field = object.getClass().getDeclaredField(key);
        }catch (NoSuchFieldException e) {
            try {
                field = object.getClass().getSuperclass().getDeclaredField(key);
            } catch (NoSuchFieldException e1) {
                log.error("错误类型："+e.getClass()+"\t错误信息"+e.getCause().getMessage());
            }
        }
        field.setAccessible(true);
        String type = field.getType().getName();
        try {
            if (type.equals("java.lang.Integer")) {
                field.set(object, Integer.parseInt(value));
            } else if (type.equals("java.lang.Long")) {
                field.set(object, Long.parseLong(value));
            } else if (type.equals("java.lang.Float")) {
                field.set(object, Float.parseFloat(value));
            } else if (type.equals("java.lang.Boolean")) {
                field.set(object, Boolean.parseBoolean(value));
            } else if (type.equals("java.util.Date")) {
                if (value.contains(":")) {
                    field.set(object, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(value));
                } else {
                    field.set(object, new SimpleDateFormat("yyyy-MM-dd").parse(value));
                }
            } else {
                field.set(object, value);
            }
        }
        catch (NumberFormatException e){
            if(e.getMessage().equals("For input string: \"\"")){
                try {
                    field.set(object,null);
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }
            }
        }
        catch (ParseException e) {
            log.error("错误类型："+e.getClass()+"\t错误信息"+e.getCause().getMessage());
        } catch (IllegalAccessException e) {
            log.error("错误类型："+e.getClass()+"\t错误信息"+e.getCause().getMessage());
        }
        return object;
    }

    /**
     * 获取子节点
     * @param classname
     * @param Field
     * @return
     */
    public String getChildNode(String classname,String Field){
        Field [] fields=null;
        try {
            fields=Class.forName(classname).getDeclaredFields();
            for(Field field:fields){
                if(field.getName().equals(Field)){
                    if(field.getType().getName().equals("java.util.List")){
                        String genericType = field.getGenericType().toString().replaceAll("java.util.List<|>","");
                        return genericType;
                    }else {
                        return field.getType().getName();
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            log.error("错误类型："+e.getClass()+"\t错误信息"+e.getCause().getMessage());
        }
        return null;
    }

    /**
     * 获取父节点
     * @return
     */
    public String getParentsNode(String classname){
        Class c= null;
        try {
            c = Class.forName(classname);
            return c.getMethod("getParents").invoke(c.newInstance()).toString();
        } catch (ClassNotFoundException e) {
            log.error("错误类型："+e.getClass()+"\t错误信息"+e.getCause().getMessage());
        } catch (NoSuchMethodException e) {
            log.error("错误类型："+e.getClass()+"\t错误信息"+e.getCause().getMessage());
        } catch (IllegalAccessException e) {
            log.error("错误类型："+e.getClass()+"\t错误信息"+e.getCause().getMessage());
        } catch (InstantiationException e) {
            log.error("错误类型："+e.getClass()+"\t错误信息"+e.getCause().getMessage());
        } catch (InvocationTargetException e) {
            log.error("错误类型："+e.getClass()+"\t错误信息"+e.getCause().getMessage());
        }
        return null;
    }

    /**
     * 根据类名获取对象
     * @param classname
     * @return
     */
    public Object getClass(String classname){
        try {
            return Class.forName(classname).newInstance();
        } catch (InstantiationException e) {
            log.error("错误类型："+e.getClass()+"\t错误信息"+e.getCause().getMessage());
        } catch (IllegalAccessException e) {
            log.error("错误类型："+e.getClass()+"\t错误信息"+e.getCause().getMessage());
        } catch (ClassNotFoundException e) {
            log.error("错误类型："+e.getClass()+"\t错误信息"+e.getCause().getMessage());
        }
        return null;
    }

    /**
     * 把json对象存进map里面
     * @param jsonObject json对象
     * @param map json对象转换的实体类字典
     * @param classname 根节点名称
     * @param flag
     * @param index
     * @param cid
     * @return
     */
    public Map getMap(JSONObject jsonObject,Map map,String classname,boolean flag,int index,int cid){
        for(Object key:jsonObject.keySet()) {
            if (JSONUtils.isArray(jsonObject.get(key))) {
                JSONArray jsonArray = jsonObject.getJSONArray(key.toString());
                boolean flag_1=getChildNode(classname,key.toString()).equals(String.class.getName());
                classname = flag_1?classname:getChildNode(classname,key.toString());
                if(flag_1){
                    String value=parseByJackson(Test.url+cid,key.toString());
                    map.put(classname, setObject(map.get(Data.class.getName()), key.toString(),value));
                    continue;
                }
                for (int i=0;i<jsonArray.size();i++) {
                    index = i;
                    ((List)map.get(classname)).add(getClass(classname));
                    getMap(JSONObject.fromObject(jsonArray.get(i)), map, classname,true,index,cid);
                    setObject(((List)map.get(classname)).get(index),"cid",String.valueOf(cid));
                    setObject(((List)map.get(classname)).get(index),"id",String.valueOf(i+1));
                }
                classname=getParentsNode(classname);
            } else if (JSONUtils.isObject(jsonObject.get(key))&&!jsonObject.get(key).equals(null)) {
                JSONObject object = jsonObject.getJSONObject(key.toString());
                JSONObject jsonObject1 = jsonObject;
                String name = classname;
                classname = getChildNode(classname,key.toString());
                getMap(object, map, classname,false,index,cid);
                setObject(map.get(classname),"cid",String.valueOf(cid));
                jsonObject = jsonObject1;
                classname = getParentsNode(classname);
            } else {
                String value = jsonObject.get(key).toString();
                if(flag){
                    Object o=((List)map.get(classname)).get(index);
                    setObject(o, key.toString(), value);
                }else {
                    map.put(classname, setObject(map.get(classname), key.toString(), value));
                }
            }
        }
        if(classname.equals(Vstorage.class.getName())) {
            map.put(classname, setObject(map.get(classname), "id", String.valueOf(cid)));
        }
        return map;
    }

    public void setData(VstorageDao vstorageDao,Map<String,Object> map){
        Class c=vstorageDao.getClass();
        String name=null;
        String classnames[] = BiliUtil.getPropertie("exclude").split(",");
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
                log.error("错误类型：" + e.getClass() + "\t错误信息" + e.getCause().getMessage());
            } catch (ClassNotFoundException e) {
                log.error("错误类型：" + e.getClass() + "\t错误信息" + e.getCause().getMessage());
            }
            if (map.get(key).getClass().getName().contains("List")) {
                List list = (List) map.get(key);
                if (list.size() == 0) {
                    continue;
                }
                for (Object object : (List) map.get(key)) {
                    if (checkfieldsNaN(object)) {
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
                            log.error("错误类型：" + e1.getClass() + "\t错误信息" + e1.getCause().getMessage());
                        } catch (InvocationTargetException e1) {
                            log.error("错误类型：" + e1.getClass() + "\t错误信息" + e1.getCause().getMessage());
                        }
                    } catch (IllegalAccessException e) {
                        log.error("错误类型：" + e.getClass() + "\t错误信息" + e.getCause().getMessage());
                    }
                }

            } else {
                if (checkfieldsNaN(map.get(key))) {
                    continue;
                }
                try {
                    insertMethod.invoke(vstorageDao, map.get(key));
                } catch (InvocationTargetException e) {
                    if(e.getTargetException().getClass().equals(DuplicateKeyException.class)){
                        log.info("更新"+name+"主键："+key+"信息");
                    }else{
                        log.error("错误类型：" + e.getClass() + "\t错误信息" + e.getCause().getMessage());
                    }
                } catch (IllegalAccessException e) {
                    log.error("错误类型：" + e.getClass() + "\t错误信息" + e.getCause().getMessage());
                }
            }
        }
    }

    public boolean checkfieldsNaN(Object object){
        Class _class= null;
        Field fields[]=null;
        try {
            _class = object.getClass();
            fields=_class.getDeclaredFields();
            for(Field field:fields){
                field.setAccessible(true);
                if(field.get(object)!=null){
                    return false;
                }
            }
            return true;
        } catch (IllegalAccessException e) {
            log.error("错误类型："+e.getClass()+"\t错误信息"+e.getCause().getMessage());
        }
        return true;
    }

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
            log.error("错误类型："+e.getClass()+"\t错误信息"+e.getCause().getMessage());
        } catch (JsonMappingException e) {
            log.error("错误类型："+e.getClass()+"\t错误信息"+e.getCause().getMessage());
        } catch (MalformedURLException e) {
            log.error("错误类型："+e.getClass()+"\t错误信息"+e.getCause().getMessage());
        } catch (IOException e) {
            log.error("错误类型："+e.getClass()+"\t错误信息"+e.getCause().getMessage());
        }
        return  null;
    }

    public int restoreCid(int id){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            log.error("错误类型："+e.getClass()+"\t错误信息"+e.getCause().getMessage());
        }
        threadPoolTaskExecutor.getThreadPoolExecutor().getQueue().clear();
        InsertServiceImpl.count=0;
        return biliDao.getAid(id);
    }

    @org.junit.Test
    public void testVstorage() {
        int speed=500;
        for (int cid = biliDao.getAid(3); ;cid++) {
            if(InsertServiceImpl.count>=10){
                cid=restoreCid(3);
                speed=500;
            }else{
                speed=100;
            }
            TaskVstorage taskVstorage=new TaskVstorage(cid,insertService);
            excute(threadPoolTaskExecutor,taskVstorage,speed);
        }
    }

    @org.junit.Test
    public void testThread(){
        int speed=500;
        for(int cid=biliDao.getAid(2);;cid++){
            if(InsertServiceImpl.count>=10){
                cid=restoreCid(2);
                speed=500;
            }else{
                speed=100;
            }
            TaskCid taskCid=new TaskCid(cid,insertService);
            excute(threadPoolTaskExecutor,taskCid,speed);
        }

    }

    public void excute(ThreadPoolTaskExecutor threadPoolTaskExecutor,Runnable runnable,int speed){
        try {
            threadPoolTaskExecutor.execute(runnable);
            Thread.sleep(speed);
        }
        catch (TaskRejectedException e2){
            log.error("队列任务已满，线程休息1小时");
            try {
                Thread.sleep(3600000);
            } catch (InterruptedException e) {
                log.error("错误类型："+e.getClass()+"\t错误信息"+e.getCause().getMessage());
            }
        }
        catch (InterruptedException e) {
            log.error("错误类型："+e.getClass()+"\t错误信息"+e.getCause().getMessage());
        }
    }

    @org.junit.Test
    public void testCid(){
        int cid=biliDao.getAid(2);
        while (true){
            insertService.insertCid(cid);
            cid++;
        }
    }

    @org.junit.Test
    public void test01(){
        insertService.insertBili(biliDao.getAid(1),1);
    }

}
