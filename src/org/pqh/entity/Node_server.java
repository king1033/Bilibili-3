package org.pqh.entity;

/**
 * Created by 10295 on 2016/5/21.
 */
public class Node_server implements Node{
    private Integer cid;
    private String node_id;
    private String server_id;
    private String sid;
    private String ip;
    private String perferred_zones;
    private String domain;
    private String allow_upload;

    @Override
    public String toString() {
        return "Node_server{" +
                "cid=" + cid +
                ", node_id='" + node_id + '\'' +
                ", server_id='" + server_id + '\'' +
                ", sid='" + sid + '\'' +
                ", ip='" + ip + '\'' +
                ", perferred_zones='" + perferred_zones + '\'' +
                ", domain='" + domain + '\'' +
                ", allow_upload='" + allow_upload + '\'' +
                '}';
    }

    @Override
    public String getParents() {
        return Upload.class.getName();
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getNode_id() {
        return node_id;
    }

    public void setNode_id(String node_id) {
        this.node_id = node_id;
    }

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPerferred_zones() {
        return perferred_zones;
    }

    public void setPerferred_zones(String perferred_zones) {
        this.perferred_zones = perferred_zones;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getAllow_upload() {
        return allow_upload;
    }

    public void setAllow_upload(String allow_upload) {
        this.allow_upload = allow_upload;
    }
}
