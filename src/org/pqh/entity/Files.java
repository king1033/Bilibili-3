package org.pqh.entity;

/**
 * Created by 10295 on 2016/5/21.
 */
public class Files extends BiliModel implements Node {
    private Integer order;
    private Integer length;
    private Long filesize;
    private String format;
    private String path;
    private Integer storage_state;
    private String md5;

    @Override
    public String toString() {
        return "Files{" +
                "id="+id+
                ",cid="+cid+
                ",order=" + order +
                ", length=" + length +
                ", filesize=" + filesize +
                ", format='" + format + '\'' +
                ", path='" + path + '\'' +
                ", storage_state=" + storage_state +
                ", md5='" + md5 + '\'' +
                '}';
    }

    @Override
    public String getParents() {
        return Data.class.getName();
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Long getFilesize() {
        return filesize;
    }

    public void setFilesize(Long filesize) {
        this.filesize = filesize;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getStorage_state() {
        return storage_state;
    }

    public void setStorage_state(Integer storage_state) {
        this.storage_state = storage_state;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}

