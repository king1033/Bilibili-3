package org.pqh.entity;

/**
 * Created by 10295 on 2016/7/10.
 */
public class BtAcg {
    private String releaseTime;
    private String type;
    private String resourceName;
    private String size;
    private String author;
    private String href;

    public BtAcg(){

    }
    public BtAcg(String resourceName, String type, String author) {
        this.resourceName = resourceName;
        this.type = type;
        this.author = author;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public String getType() {
        return type;
    }

    public String getSize() {
        return size;
    }

    public String getAuthor() {
        return author;
    }

    public String getHref() {
        return href;
    }
}
