# Bilibili<br/>
哔哩哔哩爬虫系统<br/>
开发语言：<br/>
Java(JDK7)<br/>
项目框架：<br/>
SpringMVC+MyBatis<br/>
数据库：<br/>
Mysql5.7<br/>
开发IDE：<br/>
IDEA 15<br/>
部署Web容器：<br/>
Tomcat7<br/>
----------------------------------------------<br/>
<h1 style="color:red">实现功能：</h1><br/>
<h2 style="color:green">1.获取以下三个接口信息并写入数据库</h2><br/>
http://api.bilibili.com/view<br/>
http://interface.bilibili.com/player?id=cid:<br/>
http://api.bilibili.com/vstorage/state?cid=<br/>
<br/>
<h2 style="color:green">2.获取天使论坛当季番剧音乐资源</h2><br/>
<h2 style="color:green">3.根据关键字获取bt.acg.gg上的动画资源的种子链接</h2><br/>
![image](https://github.com/luffy9412/Bilibili/blob/master/WebContent/image/btacg.png)<br/>
<h2 style="color:green">4.对哔哩哔哩201616年7月版权番剧单集平均播放量定时进行统计，                          （相关活动页见：http://www.bilibili.com/html/activity-20160620newbangumi.html）<br/>
并用js echart库以图表形式展示</h2><br/>
<h3 style="color:blue">echart折线图</h3><br/>
![image](https://github.com/luffy9412/Bilibili/blob/master/WebContent/image/echart%e6%8a%98%e7%ba%bf%e5%9b%be.png)<br/>
<h3 style="color:blue">echart柱状图</h3><br/>
![image](https://github.com/luffy9412/Bilibili/blob/master/WebContent/image/echart%e6%9f%b1%e7%8a%b6%e5%9b%be.png)<br/>
<br/>
<h3 style="color:blue">动态效果见(动态图接近15M,小水管慎重打开)：</h3><br/>
WebContent\image\echart动态展示效果.gif<br/>
-----------------------------------------------------------------------------------------------------------------<br/>
项目初始化：<br/>
第一步：当然就是Clone项目到本地，自带项目IDEA配置文件，直接用IDEA Clone不需要配置，其他IDE需要自行配置。<br/>
第二步：用src目录下的bilibili.sql创建数据库<br/>
第三步：方法调试详见<br/>
![image](https://github.com/luffy9412/Bilibili/blob/master/WebContent/image/%e6%96%b9%e6%b3%95%e6%b5%8b%e8%af%95.png)<br/>
备注：测试接口初始化类会读取bilibli账号密码配置，，如果为空或填写不正确会报错，如果不需要获取接口一信息可以直接注释掉<br/>
        详见：<br/>
        ![image](https://github.com/luffy9412/Bilibili/blob/master/WebContent/image/%e6%8a%a5%e9%94%99%e8%af%b7%e7%9c%8b.png)<br/>
        账号密码配置<br/>
        ![image](https://github.com/luffy9412/Bilibili/blob/master/WebContent/image/%e8%b4%a6%e5%8f%b7%e5%af%86%e7%a0%81%e9%85%8d%e7%bd%ae.png)<br/>
-------------------------------------------------------------------------------------------------------------------------------------------------<br/>
数据库打包到OneDriver：<br/>
https://1drv.ms/u/s!AqIrS5Y3YYnjjwi2ViTDFeAryonE<br/>
数据库文件信息自行校验<br/>
大小：	524, 720, 850 字节<br/>
修改时间：2016-07-17 00:35:20<br/>
MD5：	4C38CB6DB8F887447F28E4D1D0876DCB<br/>
SHA1：	2B402A34B698F61E9BE0171FF51C372AE8B4F1B6<br/>
CRC32：C50497BB<br/>
压缩包解压密码“A班姬路”，也是我贴吧ID,关于项目问题可以留言。<br/>
数据库如无意外会每天更新一次，视网络情况而定<br/>
以上~~~~~~~~~~~~~~~~~~~~~~<br/>