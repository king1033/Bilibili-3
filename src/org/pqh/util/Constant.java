package org.pqh.util;

/**
 * Created by 10295 on 2016/7/16.
 */
public class Constant {
    //要反射的实体类，http://api.bilibili.com/vstorage/state接口json数据注入的类
    public static String CLASSNAME="org.pqh.entity.Vstorage,org.pqh.entity.Data,java.util.ArrayList<org.pqh.entity.Files>,java.util.ArrayList<org.pqh.entity.Dispatch_servers>,org.pqh.entity.Upload,org.pqh.entity.Node_server,java.util.ArrayList<org.pqh.entity.Upload_meta>";
    //get,post请求标记
    public static String GET="get";
    public static String POST="post";
    //日期格式
    public static String DATE="yyyy-MM-dd";
    public static String DATETIME="yyyy-MM-dd HH:mm:ss";
    //http://interface.bilibili.com接口要捕捉的字段信息
    public static  String CIDLIST[]=new String[]{"maxlimit","chatid","server","vtype","oriurl","aid","typeid","pid","click","favourites","credits","coins","fw_click","duration","arctype","danmu","bottom","sinapi","acceptguest","acceptaccel"};
    //bilibili视频一级分类
    public static String dougan[]={"MAD·AMV", "MMD·3D", "短片·手书·配音", "综合"};
    public static String bangumi[]={"连载动画", "完结动画", "资讯", "官方延伸", "国产动画", "新番Index"};
    public static String music[]={"翻唱", "VOCALOID·UTAU", "演奏", "三次元音乐", "同人音乐", "OP/ED/OST", "音乐选集"};
    public static String dance[]={"宅舞", "三次元舞蹈", "舞蹈教程"};
    public static String game[]={"单机联机", "网游·电竞", "音游", "Mugen", "GMV"};
    public static String technology[]={"纪录片", "趣味科普人文", "野生技术协会", "演讲•公开课", "军事", "数码", "机械"};
    public static String ent[]={"搞笑", "生活", "动物圈", "美食圈", "综艺", "娱乐圈", "Korea相关"};
    public static String kichiku[]={"二次元鬼畜", "三次元鬼畜", "人力VOCALOID", "教程演示"};
    public static String movie[]={"欧美电影", "日本电影", "国产电影", "其他国家", "短片", "电影相关"};
    public static String teleplay[]={"连载剧集", "完结剧集", "特摄·布袋", "电视剧相关"};
    public static String fashion[]={"美妆健身", "服饰", "资讯"};
    //获取代理网址
    public static String PROXYURL="http://www.xicidaili.com/nn";
    //用AV号读取视频信息接口
    public static String AIDAPI="http://api.bilibili.com/view?type=xml&appkey=12737ff7776f1ade&access_key=";
    //用弹幕编号读取视频信息接口
    public static String CIDAPI="http://interface.bilibili.com/player?id=cid:";
    //获取access_key的接口
    public static String ACCESS_KEY="https://account.bilibili.com/api/login/v2?";
    //用弹幕编号读取视频信息接口（包括被删视频信息）
    public static String VSTORAGEAPI="http://api.bilibili.com/vstorage/state?cid=";
    //获取正版番剧信息接口
    public static String  BANGUMIAPI= "http://www.bilibili.com/api_proxy?app=bangumi&action=get_season_by_tag_v2&tag_id=143&page=1&pagesize=50&indexType=1";
    //番剧专题URL
    public static String  BANGUMIURL="http://bangumi.bilibili.com/anime/";
    //episode_id转换av_id接口
    public static String CHANGEAPI= "http://bangumi.bilibili.com/web_api/episode/get_source?episode_id=";
    //获取剧番详细信息api
    public static String BGMIDAPI="http://bangumi.bilibili.com/jsonp/seasoninfo/";
    //百度百科首页
    public static String BAIKEINDEX="http://baike.baidu.com";
    //BtAcg动画种子资源搜索网址
    public static String BTACGSEARCH="http://bt.acg.gg/search.php?keyword=";
    //BtAcg
    public static String BTACGINDEX="http://bt.acg.gg/";
    //简繁体转换接口
    public static String ZHCONVERT="http://tool.lu/zhconvert/ajax.html";
    //百度云
    public static String YUNPAN ="http://pan.baidu.com";
    //天使论坛音乐索引页面
    public static String TSDM_MUSIC_INDEX="http://www.tsdm.net/forum.php?mod=viewthread&tid=104454";
    //弹幕xml文档地址
    public static String DANMU="http://comment.bilibili.com/";
    //生成二维码接口
    public static String QRCODE="http://qr.topscan.com/api.php?text=";
    //在线抠图主页
    public static String ASSQQL="http://www.asqql.com/gifsgz/";
    //生成闪光文字图片接口
    public static String WORDART=ASSQQL+"index_s.php?mid=2016-07-24%2002:02:24&tbgcolor=ffffff&tfontsize=20&isresize=0&tfont=0007.ttf&turl=http://www.asqql.com/gifsgz/demo.jpg&ttxt=";
    //闪光文字图片生成地址
    public static String WORDARTPATH="http://www.asqql.com/upfile/u/";
    //百度云群组发送消息地址
    public static String BduSendMsg="http://pan.baidu.com/mbox/msg/send";
}
