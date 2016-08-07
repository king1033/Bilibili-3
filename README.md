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
<h1 >实现功能：</h1><br/>
<h2 >1.获取以下三个接口信息并写入数据库</h2><br/>
接口一：http://api.bilibili.com/view<br/>
接口二：http://interface.bilibili.com/player?id=cid:<br/>
接口三：http://api.bilibili.com/vstorage/state?cid=<br/>
然后这三个接口的数据有什么卵用？</br>
<a href="http://photo.weibo.com/5252060298/wbphotos/large/mid/3998341572173949/pid/005Jr6NYgw1f5x82npzo1g310b0lse8e">接口数据作用一睹为快</a></br>
就是拿来看视频用的，应该有小伙伴尝试过用黑科技通过aid(AV号)或者cid(弹幕号)下架视频，这些接口就是用</br>
来收集这些数据的，虽然现在已经有很多好心人分享各种弹幕包，但是资源是在别人手中，资源失效或者不是自</br>
己想要的那是多尴尬，所以最好的办法就是资源放在自己手中，知道bilibli整个网站视频的数据，想撸什么片就</br>
撸什么片。目前2016年7月18日为止，数据库入库数据cid有876万条，包括审核不过、被删、下架的数据，所以上</br>
面 To Love Ru Darkness 2nd 搜这部番剧相关数据用了将近八分钟，当然跟数据库结构没做过优化有关，不过我建议</br>
弹幕数据提前备份，当然几分钟时间看一下鬼畜什么的一会儿就过去了。</br>
 
我每天都有把数据库放到服务器习惯，目前用的OneDriver，<a href="#down">下载链接</a></br>
本来还想同步到百度云，不过暂时没有想到用java把文件自动上传到网盘的方案，毕竟我只是个菜鸡，如果哪位大神有</br>
什么办法希望可以指点一二o(*≧▽≦)ツ</br>

<h2 >2.获取天使论坛当季番剧音乐资源</h2><br/>
<h2 >3.根据关键字获取bt.acg.gg上的动画资源的种子链接</h2><br/>
![image](https://github.com/luffy9412/Bilibili/blob/master/WebContent/image/btacg.png)<br/>
<h2 >4.对哔哩哔哩201616年7月版权番剧单集平均播放量定时进行统计，                          （相关活动页见：http://www.bilibili.com/html/activity-20160620newbangumi.html）<br/>
并用js echart库以图表形式展示</h2><br/>
<h3 >echart折线图</h3><br/>
![image](https://github.com/luffy9412/Bilibili/blob/master/WebContent/image/echart折线图.png)<br/>
<h3 >echart柱状图</h3><br/>
![image](https://github.com/luffy9412/Bilibili/blob/master/WebContent/image/echart柱状图.png)<br/>
<br/>
<a href="http://photo.weibo.com/5252060298/wbphotos/large/mid/3998282424045592/pid/005Jr6NYgw1f5x1fnohh7g30zg0i07wv">echart动态效果</a><br/>
-----------------------------------------------------------------------------------------------------------------<br/>
项目初始化：<br/>
第一步：当然就是Clone项目到本地，自带项目IDEA配置文件，直接用<a href="http://photo.weibo.com/5252060298/wbphotos/large/mid/3998323147026568/pid/005Jr6NYgw1f5x65v49sag310b0klu0x">IDEA Clone</a>只需要配置好tomcat路径以及jar包路径即可。<a href="http://photo.weibo.com/5252060298/wbphotos/large/mid/3998282424045592/pid/005Jr6NYgw1f5x1hv77cog30tx0l0b29">IDEA Tomcat配置<a/><br/>
第二步：<a href="http://pan.baidu.com/s/1mio67Kc">下载项目jar包</a>，这里没有同步到git仓库原因是spring和mybatis框架以及杂七杂八用到的jar包将近20M的体积，不方便clone进行同步，所以另外放到云盘</br>
jar包下载完后放到项目目录里面，并进行构造路径设置。</br>
![image](https://github.com/luffy9412/Bilibili/blob/master/WebContent/image/包路径设置.png)
第三步：用src目录下的bilibili.sql创建数据库<br/>
![image](https://github.com/luffy9412/Bilibili/blob/master/WebContent/image/创建数据库.png)<br/>
如无意外创建完毕表结构应该跟下图一样。
数据库结构图
![image](https://github.com/luffy9412/Bilibili/blob/master/WebContent/image/数据库结构详解.png)<br/>
<a href="http://photo.weibo.com/5252060298/wbphotos/large/mid/3998288506219148/pid/005Jr6NYgw1f5x22mc4zlg30x40bye81">数据库部分数据展示</a>
第四步：方法调试<br/>
![image](https://github.com/luffy9412/Bilibili/blob/master/WebContent/image/方法测试.png)<br/>
判断爬虫程序是否正常运行看<a href="http://ww4.sinaimg.cn/large/005Jr6NYgw1f6lnjsvu1rg30yq0mgb2c.gif>save表</a>这个存档表。接口数据正常存入数据库的情况下，里面分别保存的当前爬取进度应该是不断刷新的<br/>
备注：调试接口一需要配置bilibli账号密码，否则会报错，如图所示：
![image](https://github.com/luffy9412/Bilibili/blob/master/WebContent/image/bilibili账号密码配置.png)<br/>
-------------------------------------------------------------------------------------------------------------------------------------------------<br/>



数据库打包到OneDriver以及百度云：<br/>
<a name='down' href='https://1drv.ms/f/s!AqIrS5Y3YYnjg00rhqs5pOw6KO4n'>OneDriver</a><br/>
<a href='http://pan.baidu.com/s/1kUSnXKN'>百度云</a><br/>
压缩包解压密码“A班姬路”，也是我贴吧ID,关于项目问题可以<a href="http://tieba.baidu.com/im/pcmsg?from=820363216">私信</a>。<br/>
数据库如无意外会每天更新一次，视网络情况而定<br/>
以上~~~~~~~~~~~~~~~~~~~~~~<br/>
