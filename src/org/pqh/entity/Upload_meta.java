package org.pqh.entity;

import java.util.Date;

/**
 * Created by 10295 on 2016/5/21.
 */
public class Upload_meta extends BiliModel implements Node{
    private Integer storage_server;
    private Integer storage_state;
    private String format;
    private Integer vp;
    private Integer create;
    private Date create_at;
    private Integer done;
    private Date done_at;

    @Override
    public String toString() {
        return "Upload_meta{" +
                "id="+id+
                ",cid="+cid+
                ",storage_server=" + storage_server +
                ", storage_state=" + storage_state +
                ", format='" + format + '\'' +
                ", vp=" + vp +
                ", create=" + create +
                ", create_at=" + create_at +
                ", done=" + done +
                ", done_at=" + done_at +
                '}';
    }

    @Override
    public String getParents() {
        return Data.class.getName();
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getStorage_server() {
        return storage_server;
    }

    public void setStorage_server(Integer storage_server) {
        this.storage_server = storage_server;
    }

    public Integer getStorage_state() {
        return storage_state;
    }

    public void setStorage_state(Integer storage_state) {
        this.storage_state = storage_state;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Integer getVp() {
        return vp;
    }

    public void setVp(Integer vp) {
        this.vp = vp;
    }

    public Integer getCreate() {
        return create;
    }

    public void setCreate(Integer create) {
        this.create = create;
    }

    public Date getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Date create_at) {
        this.create_at = create_at;
    }

    public Integer getDone() {
        return done;
    }

    public void setDone(Integer done) {
        this.done = done;
    }

    public Date getDone_at() {
        return done_at;
    }

    public void setDone_at(Date done_at) {
        this.done_at = done_at;
    }
}
