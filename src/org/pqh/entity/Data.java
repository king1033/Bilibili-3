package org.pqh.entity;

import java.io.Serializable;
import java.util.List;

public class Data implements Serializable,Node{
	private Integer cid;
	private String  type;
	private String backup_vid;
	private String letv_vu;
	private Integer letv_vid;
	private String vid;
	private Integer aid;
	private Integer page;
	private String title;
	private Integer mid;
	private String author;
	private String cover;
	private String cache;
	private Integer dispatch;
	private Integer storage;
	private Integer storage_server;
	private Integer vp;
	private List<Files> files;
	private Boolean dp_done;
	private Boolean dp_done_flv;
	private Boolean dp_done_mp4;
	private Boolean dp_done_hdmp4;
	private List<Dispatch_servers> dispatch_servers;
	private String subtitle;
	private Upload upload;
	private List<Upload_meta> upload_meta;
	private Float duration;
	private String letv_addr;

	public Data(){};

	public Data(Integer cid, String type, String backup_vid, String letv_vu, Integer letv_vid, String vid, Integer aid, Integer page, String title, Integer mid, String author, String cover, String cache, Integer dispatch, Integer storage, Integer storage_server, Integer vp, List<Files> files, Boolean dp_done, Boolean dp_done_flv, Boolean dp_done_mp4, Boolean dp_done_hdmp4, List<Dispatch_servers> dispatch_servers, String subtitle, Upload upload, List<Upload_meta> upload_meta, Float duration, String letv_addr) {
		this.cid = cid;
		this.type = type;
		this.backup_vid = backup_vid;
		this.letv_vu = letv_vu;
		this.letv_vid = letv_vid;
		this.vid = vid;
		this.aid = aid;
		this.page = page;
		this.title = title;
		this.mid = mid;
		this.author = author;
		this.cover = cover;
		this.cache = cache;
		this.dispatch = dispatch;
		this.storage = storage;
		this.storage_server = storage_server;
		this.vp = vp;
		this.files = files;
		this.dp_done = dp_done;
		this.dp_done_flv = dp_done_flv;
		this.dp_done_mp4 = dp_done_mp4;
		this.dp_done_hdmp4 = dp_done_hdmp4;
		this.dispatch_servers = dispatch_servers;
		this.subtitle = subtitle;
		this.upload = upload;
		this.upload_meta = upload_meta;
		this.duration = duration;
		this.letv_addr = letv_addr;
	}

	@Override
	public String toString() {
		return "Data{" +
				"cid=" + cid +
				", type='" + type + '\'' +
				", backup_vid='" + backup_vid + '\'' +
				", letv_vu='" + letv_vu + '\'' +
				", letv_vid=" + letv_vid +
				", vid='" + vid + '\'' +
				", aid=" + aid +
				", page=" + page +
				", title='" + title + '\'' +
				", mid=" + mid +
				", author='" + author + '\'' +
				", cover='" + cover + '\'' +
				", cache='" + cache + '\'' +
				", dispatch=" + dispatch +
				", storage=" + storage +
				", storage_server=" + storage_server +
				", vp=" + vp +
				", files=" + files +
				", dp_done=" + dp_done +
				", dp_done_flv=" + dp_done_flv +
				", dp_done_mp4=" + dp_done_mp4 +
				", dp_done_hdmp4=" + dp_done_hdmp4 +
				", dispatch_servers=" + dispatch_servers +
				", subtitle='" + subtitle + '\'' +
				", upload=" + upload +
				", upload_meta=" + upload_meta +
				", duration=" + duration +
				", letv_addr='" + letv_addr + '\'' +
				'}';
	}

	@Override
	public String getParents() {
		return Vstorage.class.getName();
	}

	public List<Dispatch_servers> getDispatch_servers() {
		return dispatch_servers;
	}

	public void setDispatch_servers(List<Dispatch_servers> dispatch_servers) {
		this.dispatch_servers = dispatch_servers;
	}

	public String getLetv_addr() {
		return letv_addr;
	}

	public void setLetv_addr(String letv_addr) {
		this.letv_addr = letv_addr;
	}

	public List<Files> getFiles() {
		return files;
	}

	public void setFiles(List<Files> files) {
		this.files = files;
	}

	public Boolean getDp_done() {
		return dp_done;
	}

	public void setDp_done(Boolean dp_done) {
		this.dp_done = dp_done;
	}

	public Boolean getDp_done_flv() {
		return dp_done_flv;
	}

	public void setDp_done_flv(Boolean dp_done_flv) {
		this.dp_done_flv = dp_done_flv;
	}

	public Boolean getDp_done_mp4() {
		return dp_done_mp4;
	}

	public void setDp_done_mp4(Boolean dp_done_mp4) {
		this.dp_done_mp4 = dp_done_mp4;
	}

	public Boolean getDp_done_hdmp4() {
		return dp_done_hdmp4;
	}

	public void setDp_done_hdmp4(Boolean dp_done_hdmp4) {
		this.dp_done_hdmp4 = dp_done_hdmp4;
	}

	public List<Dispatch_servers> getDispatch_serverses() {
		return dispatch_servers;
	}

	public void setDispatch_serverses(List<Dispatch_servers> dispatch_serverses) {
		this.dispatch_servers = dispatch_serverses;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle(String subtitle) {
		this.subtitle = subtitle;
	}

	public Upload getUpload() {
		return upload;
	}

	public void setUpload(Upload upload) {
		this.upload = upload;
	}

	public List<Upload_meta> getUpload_meta() {
		return upload_meta;
	}

	public void setUpload_meta(List<Upload_meta> upload_meta) {
		this.upload_meta = upload_meta;
	}

	public Float getDuration() {
		return duration;
	}

	public void setDuration(Float duration) {
		this.duration = duration;
	}

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBackup_vid() {
		return backup_vid;
	}

	public void setBackup_vid(String backup_vid) {
		this.backup_vid = backup_vid;
	}

	public String getLetv_vu() {
		return letv_vu;
	}

	public void setLetv_vu(String letv_vu) {
		this.letv_vu = letv_vu;
	}

	public Integer getLetv_vid() {
		return letv_vid;
	}

	public void setLetv_vid(Integer letv_vid) {
		this.letv_vid = letv_vid;
	}

	public String getVid() {
		return vid;
	}

	public void setVid(String vid) {
		this.vid = vid;
	}

	public Integer getAid() {
		return aid;
	}

	public void setAid(Integer aid) {
		this.aid = aid;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getMid() {
		return mid;
	}

	public void setMid(Integer mid) {
		this.mid = mid;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getCache() {
		return cache;
	}

	public void setCache(String cache) {
		this.cache = cache;
	}

	public Integer getDispatch() {
		return dispatch;
	}

	public void setDispatch(Integer dispatch) {
		this.dispatch = dispatch;
	}

	public Integer getStorage() {
		return storage;
	}

	public void setStorage(Integer storage) {
		this.storage = storage;
	}

	public Integer getStorage_server() {
		return storage_server;
	}

	public void setStorage_server(Integer storage_server) {
		this.storage_server = storage_server;
	}

	public Integer getVp() {
		return vp;
	}

	public void setVp(Integer vp) {
		this.vp = vp;
	}
}