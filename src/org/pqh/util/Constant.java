package org.pqh.util;

/**
 * Created by 10295 on 2016/7/16.
 */
public class Constant {
    //要反射的实体类，http://api.bilibili.com/vstorage/state接口json数据注入的类
    public static final String CLASSNAME="org.pqh.entity.Vstorage,org.pqh.entity.Data,java.util.ArrayList<org.pqh.entity.Files>,java.util.ArrayList<org.pqh.entity.Dispatch_servers>,org.pqh.entity.Upload,org.pqh.entity.Node_server,java.util.ArrayList<org.pqh.entity.Upload_meta>";
    //get,post请求标记
    public static final String GET="get";
    public static final String POST="post";
    //http://interface.bilibili.com接口要捕捉的字段信息
    public static final  String CIDLIST[]=new String[]{"maxlimit","chatid","server","vtype","oriurl","aid","typeid","pid","click","favourites","credits","coins","fw_click","duration","arctype","danmu","bottom","sinapi","acceptguest","acceptaccel"};
    //bilibili视频一级分类
    public static final String dougan[]={"MAD·AMV", "MMD·3D", "短片·手书·配音", "综合"};
    public static final String bangumi[]={"连载动画", "完结动画", "资讯", "官方延伸", "国产动画", "新番Index"};
    public static final String music[]={"翻唱", "VOCALOID·UTAU", "演奏", "三次元音乐", "同人音乐", "OP/ED/OST", "音乐选集"};
    public static final String dance[]={"宅舞", "三次元舞蹈", "舞蹈教程"};
    public static final String game[]={"单机联机", "网游·电竞", "音游", "Mugen", "GMV"};
    public static final String technology[]={"纪录片", "趣味科普人文", "野生技术协会", "演讲•公开课", "军事", "数码", "机械"};
    public static final String ent[]={"搞笑", "生活", "动物圈", "美食圈", "综艺", "娱乐圈", "Korea相关"};
    public static final String kichiku[]={"二次元鬼畜", "三次元鬼畜", "人力VOCALOID", "教程演示"};
    public static final String movie[]={"欧美电影", "日本电影", "国产电影", "其他国家", "短片", "电影相关"};
    public static final String teleplay[]={"连载剧集", "完结剧集", "特摄·布袋", "电视剧相关"};
    public static final String fashion[]={"美妆健身", "服饰", "资讯"};
    //获取代理网址
    public static final String PROXYURL="http://www.xicidaili.com/nn";
    //用AV号读取视频信息接口
    public static final String AIDAPI="http://api.bilibili.com/view?type=xml&appkey=12737ff7776f1ade&access_key="+BiliUtil.getAccesskey(BiliUtil.getPropertie("biliusername"),BiliUtil.getPropertie("bilipwd"));;
    //用弹幕编号读取视频信息接口
    public static final String CIDAPI="http://interface.bilibili.com/player?id=cid:";
    //获取access_key的接口
    public static final String ACCESS_KEY="https://account.bilibili.com/api/login/v2?";
    //用弹幕编号读取视频信息接口（包括被删视频信息）
    public static final String VSTORAGEAPI="http://api.bilibili.com/vstorage/state?cid=";
    //获取正版番剧信息接口
    public static final String  BANGUMIAPI= "http://www.bilibili.com/api_proxy?app=bangumi&action=get_season_by_tag_v2&tag_id=143&page=1&pagesize=50&indexType=1";
    //番剧专题URL
    public static final String  BANGUMIURL="http://bangumi.bilibili.com/anime/";
    //episode_id转换av_id接口
    public static final String CHANGEAPI= "http://bangumi.bilibili.com/web_api/episode/get_source?episode_id=";
    //获取剧番详细信息api
    public static final String BGMIDAPI="http://bangumi.bilibili.com/jsonp/seasoninfo/";
    //百度百科首页
    public static final String BAIKEINDEX="http://baike.baidu.com";
    //BtAcg动画种子资源搜索网址
    public static final String BTACG="http://bt.acg.gg/search.php?keyword=";
}
