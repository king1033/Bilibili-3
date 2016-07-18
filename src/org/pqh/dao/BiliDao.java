package org.pqh.dao;

import org.apache.ibatis.annotations.Param;
import org.pqh.entity.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface BiliDao {
	Bili findByCid(int cid);
	Bili findByAid(int aid);
	int getAid(int id);
	void setAid(@Param("aid")int aid,@Param("id")int id);
	Integer getLimit(int aid);
	void setLimit(int aid);
	List<Bili> findAids(int aid);
	List<Bili> findCids(@Param("startcid")int a,@Param("overcid")int b);
	void insertBili(Bili bili);
	void updateBili(Bili bili);
	void insertCid(Bili bili);
	void updateCid(Bili bili);
	int count(int aid);
	List<Bili> findVCC(@Param("startaid")int a,@Param("overaid")int b);

	void insertC(Map<String,Object> Cid);

	void updateC(Map<String,Object> Cid);
	//查询所有日期当天视频投稿量
	List<AvCount> selectAvCount();

	void insertAvPlay(List list);
	List<AvPlay> selectAvPlay();

	List<Ranking> selectRanking();

	void insertParam(org.pqh.entity.Param param);

	org.pqh.entity.Param selectParam(String key);

	List<org.pqh.entity.Param> selectParams();

	void updateParam(org.pqh.entity.Param param);

	void insertBangumi(Bangumi bangumi);

	void updateBangumi(Bangumi bangumi);

	Bangumi selectBangumi_id(Integer bangumi_id);


}
