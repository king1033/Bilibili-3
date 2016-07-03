package org.pqh.entity;

/**
 * Created by 10295 on 2016/7/3.
 */
public class Bangumi {
    private Integer bangumi_id;
    private Integer season_id;
    private String title;
    private Integer allow_download;

    public Integer getBangumi_id() {
        return bangumi_id;
    }

    public void setBangumi_id(Integer bangumi_id) {
        this.bangumi_id = bangumi_id;
    }

    public Integer getSeason_id() {
        return season_id;
    }

    public void setSeason_id(Integer season_id) {
        this.season_id = season_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getAllow_download() {
        return allow_download;
    }

    public void setAllow_download(Integer allow_download) {
        this.allow_download = allow_download;
    }
}
