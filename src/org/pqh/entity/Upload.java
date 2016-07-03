package org.pqh.entity;

import java.util.Date;

/**
 * Created by 10295 on 2016/5/21.
 */
public class Upload implements Node{
    private Integer cid;
    private String filename;
    private Integer timestamp;
    private Date create_at;
    private Integer uploaded;
    private Date uploaded_at;
    private Integer convert;
    private Date convert_at;
    private Integer done;
    private Date done_at;
    private String ip;
    private String encode_server;
    private Node_server node_server;
    private String state;
    private String error_reason;

    @Override
    public String toString() {
        return "Upload{" +
                "cid=" + cid +
                ", filename='" + filename + '\'' +
                ", timestamp=" + timestamp +
                ", create_at=" + create_at +
                ", uploaded=" + uploaded +
                ", uploaded_at=" + uploaded_at +
                ", convert=" + convert +
                ", convert_at=" + convert_at +
                ", done=" + done +
                ", done_at=" + done_at +
                ", ip='" + ip + '\'' +
                ", encode_server='" + encode_server + '\'' +
                ", node_server=" + node_server +
                ", state='" + state + '\'' +
                ", error_reason='" + error_reason + '\'' +
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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public Date getCreate_at() {
        return create_at;
    }

    public void setCreate_at(Date create_at) {
        this.create_at = create_at;
    }

    public Integer getUploaded() {
        return uploaded;
    }

    public void setUploaded(Integer uploaded) {
        this.uploaded = uploaded;
    }

    public Date getUploaded_at() {
        return uploaded_at;
    }

    public void setUploaded_at(Date uploaded_at) {
        this.uploaded_at = uploaded_at;
    }

    public Integer getConvert() {
        return convert;
    }

    public void setConvert(Integer convert) {
        this.convert = convert;
    }

    public Date getConvert_at() {
        return convert_at;
    }

    public void setConvert_at(Date convert_at) {
        this.convert_at = convert_at;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getEncode_server() {
        return encode_server;
    }

    public void setEncode_server(String encode_server) {
        this.encode_server = encode_server;
    }

    public Node_server getNode_server() {
        return node_server;
    }

    public void setNode_server(Node_server node_server) {
        this.node_server = node_server;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getError_reason() {
        return error_reason;
    }

    public void setError_reason(String error_reason) {
        this.error_reason = error_reason;
    }
}
