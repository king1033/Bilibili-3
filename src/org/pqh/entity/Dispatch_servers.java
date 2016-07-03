package org.pqh.entity;

import java.util.Date;

/**
 * Created by 10295 on 2016/5/21.
 */
public class Dispatch_servers extends BiliModel implements Node{
    private Integer dms_id;
    private String description;
    private Integer server;
    private String format;
    private Integer is_active;
    private Integer create;
    private Date create_at;
    private Integer done;
    private Date done_at;
    private String state;

    @Override
    public String toString() {
        return "Dispatch_servers{" +
                "id="+id+
                ",cid="+cid+
                ",dms_id=" + dms_id +
                ", description='" + description + '\'' +
                ", server=" + server +
                ", format='" + format + '\'' +
                ", is_active=" + is_active +
                ", create=" + create +
                ", create_at=" + create_at +
                ", done=" + done +
                ", done_at=" + done_at +
                ", state='" + state + '\'' +
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

    public Integer getDms_id() {
        return dms_id;
    }

    public void setDms_id(Integer dms_id) {
        this.dms_id = dms_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getServer() {
        return server;
    }

    public void setServer(Integer server) {
        this.server = server;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public Integer getIs_active() {
        return is_active;
    }

    public void setIs_active(Integer is_active) {
        this.is_active = is_active;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
