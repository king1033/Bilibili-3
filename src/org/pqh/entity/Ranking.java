package org.pqh.entity;

import org.pqh.util.TimeUtil;

import java.sql.Timestamp;

/**
 * Created by 10295 on 2016/7/6.
 */
public class Ranking {
    private Timestamp key;
    private String value;
    private String formatTime;

    public Timestamp getKey() {
        return key;
    }

    public void setKey(Timestamp key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getFormatTime() {
        return formatTime;
    }

    public void setFormatTime(String formatTime) {
        this.formatTime = formatTime;
    }

    public Ranking(Timestamp key, String value) {
        this.key = key;
        this.value = value;
        this.formatTime=TimeUtil.formatDateToString(key,null);
    }
}
